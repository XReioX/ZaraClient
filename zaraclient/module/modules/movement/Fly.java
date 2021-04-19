package zaraclient.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import zaraclient.MoveUtil;
import zaraclient.event.Event;
import zaraclient.event.events.EventMotion;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.Timer;
import zaraclient.module.setting.BooleanSetting;
import zaraclient.module.setting.ModeSetting;
import zaraclient.module.setting.NumberSetting;

public class Fly extends Module {

    public ModeSetting modeSetting = new ModeSetting("Mode", "Vanilla", "Vanilla", "ZoomFly", "Glide", "AirJump", "TestAC V1", "AAC", "Verus");
    public NumberSetting timerSetting = new NumberSetting("Timer", 1, 1, 15, 1);
    public NumberSetting speedSetting = new NumberSetting("Speed", 0.25, 0.1, 3, 0.1);
    public BooleanSetting damageFly = new BooleanSetting("Damage", false);

    public BooleanSetting vanillaKickBypass = new BooleanSetting("Vanilla Kick Bypass", false);

    public Timer vanillaTimer = new Timer();

    private void handleVanillaKickBypass() {
        if(!vanillaKickBypass.getBoolean() || !vanillaTimer.hasTimeElapsed(1000, true)) return;

        final double ground = calculateGround();

        for(double posY = mc.thePlayer.posY; posY > ground; posY -= 8D) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true));

            if(posY - 8D < ground) break; // Prevent next step
        }

        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, ground, mc.thePlayer.posZ, true));


        for(double posY = ground; posY < mc.thePlayer.posY; posY += 8D) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, posY, mc.thePlayer.posZ, true));

            if(posY + 8D > mc.thePlayer.posY) break; // Prevent next step
        }

        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));


    }

    private double calculateGround() {
        final AxisAlignedBB playerBoundingBox = mc.thePlayer.getEntityBoundingBox();
        double blockHeight = 1D;

        for(double ground = mc.thePlayer.posY; ground > 0D; ground -= blockHeight) {
            final AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);

            if(mc.theWorld.checkBlockCollision(customBox)) {
                if(blockHeight <= 0.05D)
                    return ground + blockHeight;

                ground += blockHeight;
                blockHeight = 0.05D;
            }
        }

        return 0F;
    }

    public Fly(){
        super("Fly", Keyboard.KEY_NONE, Category.MOVEMENT);

        this.addSettings(modeSetting, timerSetting, speedSetting, vanillaKickBypass, damageFly);

        this.suffix = modeSetting.getMode();
    }

    public void onChanged(){
        this.suffix = modeSetting.getMode();
    }

    public void onDisable(){
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.capabilities.allowFlying = mc.playerController.isInCreativeMode() ? true : false;

        /*
        if(mc.timer.timerSpeed != 1.0F){
            mc.timer.timerSpeed = 1F;
        }

         */
    }

    public void damage(double damage){
        Minecraft mc = Minecraft.getMinecraft();

        if (damage > MathHelper.floor_double(mc.thePlayer.getMaxHealth()))
            damage = MathHelper.floor_double(mc.thePlayer.getMaxHealth());

        double offset = 0.0625;
        //offset = 0.015625;
        if (mc.thePlayer != null && mc.getNetHandler() != null) {
            for (short i = 0; i <= ((3 + damage) / offset); i++) {
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY + ((offset / 2) * 1), mc.thePlayer.posZ, false));
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY + ((offset / 2) * 2), mc.thePlayer.posZ, false));
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                        mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + damage) / offset))));
                //mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX,
                //mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, (i == ((3 + damage) / offset))));
            }
        }
    }

    public void onEnable(){
        if(damageFly.getBoolean()){
            damage(1.0F);
        }
    }

    public void onEvent(Event e){
        if(e instanceof EventUpdate){
            handleVanillaKickBypass();
            mc.timer.timerSpeed = (float) timerSetting.getValue();
        }
        if(e instanceof EventUpdate && modeSetting.getMode().equalsIgnoreCase("Vanilla")){
            mc.thePlayer.capabilities.isFlying = true;
            mc.thePlayer.capabilities.allowFlying = true;
        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("ZoomFly")) {
            EventMotion motion = (EventMotion) e;

            motion.motionY = 0;

            if (mc.thePlayer.ticksExisted % 2 == 0.0) {
                motion.onGround = false;
                MoveUtil.strafe((float) speedSetting.getValue());
            }

        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("Verus")){

            EventMotion motion = (EventMotion) e;

            motion.motionY = 0;
            motion.onGround = false; // Verus relies on client sent ground packets, so this takes advantage of it.
            MoveUtil.strafe((float)speedSetting.getValue());


        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("Glide")){
            if(mc.thePlayer.motionY < 0){
                ((EventMotion)e).motionY = -.125d;
            }
        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("AirJump")){
            if(!mc.thePlayer.onGround){
                mc.thePlayer.onGround = true;
            }
        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("TestAC V1")){
            mc.thePlayer.motionY = 0;

            if(mc.thePlayer.ticksExisted % 2 == 0.0){
                ((EventMotion)e).onGround = true;

                MoveUtil.strafe((float)speedSetting.getValue());
            }
        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("AAC")){
            if(!mc.thePlayer.onGround){
                mc.thePlayer.motionY = 0;
                MoveUtil.strafe(0.45F);
            }
        }
    }
}
