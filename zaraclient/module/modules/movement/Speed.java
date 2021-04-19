package zaraclient.module.modules.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import zaraclient.MoveUtil;
import zaraclient.event.Event;
import zaraclient.event.events.EventMotion;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.Timer;
import zaraclient.module.setting.ModeSetting;
import zaraclient.module.setting.NumberSetting;

public class Speed extends Module {

    public ModeSetting modeSetting = new ModeSetting("Mode", "Basic", "Basic", "SimpleHop", "LowHop", "Spartan", "NCP");
    public NumberSetting speed = new NumberSetting("Speed (only for basic)", 5, 1, 10, 1);
    public Speed(){
        super("Speed", Keyboard.KEY_NONE, Category.MOVEMENT);

        this.addSettings(modeSetting, speed);

        this.suffix = modeSetting.getMode();
    }

    public Timer npcTimer = new Timer();

    public void onChanged(){
        this.suffix = modeSetting.getMode();
    }

    public void onDisable(){
        if(mc.timer.timerSpeed != 1F){
            mc.timer.timerSpeed = 1F;
        }
    }

    public void onEvent(Event e){
        if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("Basic")){
            if(mc.thePlayer.moveForward > 0){
                MoveUtil.strafe((float)speed.getValue() / 10);
            }
        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("SimpleHop")){
            if(mc.thePlayer.moveForward > 0){
                if(MoveUtil.isOnGround(0.0001)){
                    mc.thePlayer.setSprinting(true);
                    MoveUtil.strafe(0.25F);
                    mc.thePlayer.motionY = 0.419F;
                }

            }
        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("LowHop")){
            if(mc.thePlayer.moveForward > 0){

                if(MoveUtil.isOnGround(0.0001)){
                    mc.thePlayer.setSprinting(true);
                    MoveUtil.strafe(0.25F);
                    mc.thePlayer.motionY = 0.35F;
                }
            }
        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("Spartan")){

            if(mc.thePlayer.moveForward > 0 && mc.thePlayer.ticksExisted % 2==0){
                mc.timer.timerSpeed = 1.5F;
                if(MoveUtil.isOnGround(0.0001)){
                    mc.thePlayer.setSprinting(true);
                    MoveUtil.strafe(0.35F);
                    mc.thePlayer.motionY = 0.42F;
                    ((EventMotion)e).onGround = true;
                }
            }
        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("NCP")){
            if(npcTimer.hasTimeElapsed(10, true)){
                mc.timer.timerSpeed = 1.25F;
                if(MoveUtil.isOnGround(0.0001)){
                    mc.thePlayer.setSprinting(true);
                    MoveUtil.strafe(0.35F);
                    ((EventMotion)e).motionY = 0.42F;
                    ((EventMotion)e).onGround = true;
                }
            }
        }
    }
}
