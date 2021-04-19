package zaraclient.module.modules.combat;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import zaraclient.CombatUtil;
import zaraclient.ZaraClient;
import zaraclient.event.Event;
import zaraclient.event.events.Event2D;
import zaraclient.event.events.EventMotion;
import zaraclient.event.events.EventStrafe;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.Timer;
import zaraclient.module.setting.BooleanSetting;
import zaraclient.module.setting.NumberSetting;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KillAura extends Module {


    public NumberSetting range = new NumberSetting("Range", 3, 1, 15, 1);
    public BooleanSetting attackDead = new BooleanSetting("Target Dead", false);
    public BooleanSetting onlyPlayers = new BooleanSetting("Only Players", true);
    public BooleanSetting autoBlock = new BooleanSetting("Auto Block", true);
    public BooleanSetting rotationClient = new BooleanSetting("Rotation clientside", false);
    public BooleanSetting headMovement = new BooleanSetting("HeadMove", true);
    public BooleanSetting teams = new BooleanSetting("Teams", false);
    public BooleanSetting limitedRotations = new BooleanSetting("Limited Rots", false);

    public BooleanSetting autoStrafe = new BooleanSetting("Strafe", false);

    public KillAura(){
        super("KillAura", Keyboard.KEY_NONE, Category.COMBAT);

        this.addSettings(range, attackDead, onlyPlayers, autoBlock, rotationClient, autoStrafe, headMovement, teams, limitedRotations);

        this.suffix = "R: " + range.getValue();
    }

    public void onChanged(){
        this.suffix = "R: " + range.getValue();
    }

    public Entity target;

    public boolean shouldAttack(Entity entity){
        if(entity.getDistanceToEntity(mc.thePlayer) > range.value){
            return false;
        }

        if(entity == mc.thePlayer){
            return false;
        }
        if (entity.ticksExisted < 10){
            return false;
        }

        if(ZaraClient.moduleManager.getModuleByName("AntiBot").isToggled() && CombatUtil.isIgnoredEntity(entity)){

            return false;
        }

        if(entity.isDead && !attackDead.getBoolean()){
            return false;
        }





        if(onlyPlayers.getBoolean() && !(entity instanceof EntityPlayer)){
            return false;
        }

        if(teams.getBoolean() == true && entity instanceof EntityPlayer){
            if(((EntityPlayer)entity).isOnSameTeam(mc.thePlayer)){
                return false;
            }
        }

        return true;
    }

    Timer timer = new Timer();


    public boolean canBlock(){
        return mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
    }



    public void block(){

        mc.thePlayer.setItemInUse(mc.thePlayer.inventory.getCurrentItem(), 15);
    }

    public EntityLivingBase lastEntity;
    public long lastAttack;

    public void attack(Entity entity){
        mc.thePlayer.swingItem();
        mc.playerController.attackEntity(mc.thePlayer, entity);

        if(canBlock() && autoBlock.getBoolean()){
            block();
        }

        lastEntity = (EntityLivingBase) entity;
        lastAttack = System.currentTimeMillis();
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public float fixPitch(float pitch){
        if(pitch > 90F) return 90F;
        if(pitch < -90F) return -90F;

        return pitch;
    }

    FontRenderer fr = mc.fontRendererObj;


    public void onEvent(Event e){
        if(e instanceof Event2D) {
            if(Math.abs(System.currentTimeMillis() - lastAttack) < 2000 && lastEntity != null){
                ScaledResolution sr = new ScaledResolution(mc);

                boolean winning = mc.thePlayer.getHealth() > lastEntity.getHealth() ? true : false;

                fr.drawStringWithShadow(winning + " (" + lastEntity.getHealth() + ")", (sr.getScaledWidth()/2) - 65, (sr.getScaledHeight()/2) - 20, -1);

            }

        }else if(e instanceof EventMotion && e.isPre()){
            EventMotion event = (EventMotion) e;
            List<Entity> targets = mc.theWorld.loadedEntityList.stream().filter(p -> p instanceof EntityLivingBase).collect(Collectors.toList());
            targets = targets.stream().filter(p -> shouldAttack(p)).collect(Collectors.toList());

            targets.sort(Comparator.comparingDouble(s -> ((EntityLivingBase) s).getDistanceToEntity(mc.thePlayer)));

            if (!targets.isEmpty()) {
                EntityLivingBase base = (EntityLivingBase) targets.get(0);


                this.target = base;

                if(this.rotationClient.getBoolean()){
                    if(!limitedRotations.getBoolean()){
                        mc.thePlayer.rotationYaw = getRotations(base)[0];
                        mc.thePlayer.rotationPitch = fixPitch(getRotations(base)[1]);
                    }else{
                        float[] rotations = getRotations(base);
                        mc.thePlayer.rotationPitch = (float) this.limitAngleChange(mc.thePlayer.prevRotationPitch, rotations[1], 4.0F);
                        mc.thePlayer.rotationYaw = (float) this.limitAngleChange(mc.thePlayer.prevRotationYaw, rotations[0], 4.0F);
                    }
                }else{
                    if(!limitedRotations.getBoolean()){
                        event.yaw = getRotations(base)[0];
                        event.pitch = fixPitch(getRotations(base)[1]);
                    }else{
                        float[] rotations = getRotations(base);
                        event.yaw = (float) this.limitAngleChange(mc.thePlayer.prevRotationYaw, rotations[0], 4.0F);
                        event.pitch = (float) this.limitAngleChange(mc.thePlayer.prevRotationPitch, rotations[1], 4.0F);
                    }


                }

                if(this.headMovement.getBoolean()){
                    mc.thePlayer.rotationYawHead = getRotations(base)[0];
                }

                if(timer.hasTimeElapsed(150, true)){
                    attack(base);
                }



            }

        }else if(e instanceof EventStrafe && autoStrafe.getBoolean()){
            EventStrafe strafeEvent = (EventStrafe) e;

            if(target != null && !target.isDead && shouldAttack(target)){
                strafeEvent.setCancelled(true);
                float f = strafeEvent.strafe * strafeEvent.strafe + strafeEvent.forward * strafeEvent.forward;

                if (f >= 1.0E-4F)
                {
                    f = MathHelper.sqrt_float(f);

                    if (f < 1.0F)
                    {
                        f = 1.0F;
                    }

                    f = strafeEvent.friction / f;
                    strafeEvent.strafe = strafeEvent.strafe * f;
                    strafeEvent.forward = strafeEvent.forward * f;



                    float f1 = MathHelper.sin(strafeEvent.getYaw() * (float) Math.PI / 180.0F);
                    float f2 = MathHelper.cos(strafeEvent.getYaw() * (float) Math.PI / 180.0F);

                    mc.thePlayer.motionX += (double) (strafeEvent.strafe * f2 - strafeEvent.forward * f1);
                    mc.thePlayer.motionZ += (double) (strafeEvent.forward * f2 + strafeEvent.strafe * f1);
                }
            }
        }
    }


    private double limitAngleChange(double current, double intended, double speed) {
        double change = intended - current;
        if (change > speed) {
            change = speed;
        } else if (change < -speed) {
            change = -speed;
        }
        return current + change;
    }




    public float[] getRotations(EntityLivingBase ent) {
        double deltaX = ent.posX + (ent.posX - ent.lastTickPosX) - mc.thePlayer.posX,
                deltaY = ent.posY - 3.5 + ent.getEyeHeight() - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
                deltaZ = ent.posZ + (ent.posZ - ent.lastTickPosZ) - mc.thePlayer.posZ,
                distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

        float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)),
                pitch = (float) -Math.toDegrees(deltaY / distance);

        if(deltaX < 0 && deltaZ < 0){
            yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        }else if(deltaX > 0 && deltaZ < 0){
            yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        }

        return new float[] {yaw, pitch};



    }
}
