package zaraclient.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import zaraclient.CombatUtil;
import zaraclient.ZaraClient;
import zaraclient.event.Event;
import zaraclient.event.events.EventMotion;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.NumberSetting;

public class Aimbot extends Module {

    public NumberSetting rotationSpeed = new NumberSetting("Speed", 2, 1, 10, 0.5);
    public NumberSetting rangeValue = new NumberSetting("Range", 3, 1, 15, 0.5);

    public Aimbot(){
        super("Aimbot", Keyboard.KEY_NONE, Category.COMBAT);

        this.addSettings(rotationSpeed, rangeValue);
    }

    public float[] getRotations(Entity entity){
        if (entity == null) {
            return null;
        }
        double diffX = entity.posX - mc.thePlayer.posX;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        } else {
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }
        double diffZ = entity.posZ - mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch) };
    }

    private void faceEntity(Entity entity, double speed) {
        float[] rotations = this.getRotations(entity);
        if (rotations != null) {
            mc.thePlayer.rotationYaw = (float) this.limitAngleChange(mc.thePlayer.prevRotationYaw, rotations[0], speed);
            mc.thePlayer.rotationPitch = (float) this.limitAngleChange(mc.thePlayer.prevRotationPitch, rotations[1], speed);
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

    public boolean shouldRotate(Entity entity){
        if(entity.getDistanceToEntity(mc.thePlayer) > rangeValue.value){
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

        return true;
    }


    public void onEvent(Event event){
        if(event instanceof EventUpdate){
            for(Entity entity : mc.theWorld.loadedEntityList){
                if(entity != null && shouldRotate(entity)){
                    faceEntity(entity, rotationSpeed.getValue());
                }
            }
        }
    }
}
