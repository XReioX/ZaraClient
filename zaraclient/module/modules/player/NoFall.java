package zaraclient.module.modules.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import zaraclient.event.Event;
import zaraclient.event.events.EventMotion;
import zaraclient.event.events.EventPacketSend;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.ModeSetting;
import zaraclient.notifications.Notification;
import zaraclient.notifications.NotificationManager;
import zaraclient.notifications.NotificationType;

import java.awt.*;

public class NoFall extends Module {

    public ModeSetting modeSetting = new ModeSetting("Mode", "GroundSpoof", "GroundSpoof", "TickOffset", "InstantSpoof", "NoGround");

    public NoFall(){
        super("NoFall", Keyboard.KEY_NONE, Category.PLAYER);

        this.addSettings(modeSetting);

        this.suffix = modeSetting.getMode();

    }

    public void onChanged(){
        this.suffix = modeSetting.getMode();
    }

    public void onEnable(){

    }

    public void onEvent(Event e){
        if(e instanceof EventUpdate && modeSetting.getMode().equalsIgnoreCase("GroundSpoof")){
            if(mc.thePlayer.fallDistance > 2F){
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("TickOffset")){
            if(mc.thePlayer.fallDistance > 1.5F){
                ((EventMotion)e).onGround = mc.thePlayer.ticksExisted % 2 == 0.0;
            }
        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("InstantSpoof")){
            if(mc.thePlayer.fallDistance > 1.5F){
                ((EventMotion)e).onGround = true;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
            }
        }else if(e instanceof EventMotion && modeSetting.getMode().equalsIgnoreCase("NoGround")){
            ((EventMotion)e).onGround = false;
        }
    }
}
