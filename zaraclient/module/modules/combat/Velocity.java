package zaraclient.module.modules.combat;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.lwjgl.input.Keyboard;
import zaraclient.event.Event;
import zaraclient.event.events.EventPacketReceive;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.ModeSetting;
import zaraclient.module.setting.NumberSetting;

public class Velocity extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Simple", "Simple", "AllAC");

    public NumberSetting horizontal = new NumberSetting("Horizontal", 50, 0, 100, 1);
    public NumberSetting vertical = new NumberSetting("Horizontal", 50, 0, 100, 1);

    public Velocity(){
        super("Velocity", Keyboard.KEY_NONE, Category.COMBAT);

        this.addSettings(mode, horizontal, vertical);

        this.suffix = mode.getMode();
    }

    public void onChanged(){
        this.suffix = mode.getMode();

        if(this.mode.getMode().equalsIgnoreCase("Simple")){
            this.suffix = "Simple | H:" + horizontal.getValue() + "% | V:" + vertical.getValue() + "%";
        }
    }

    public void onEvent(Event event){
        if(mode.getMode().equalsIgnoreCase("AllAC") && event instanceof EventUpdate){
            if(mc.thePlayer.hurtTime > 0){
                mc.thePlayer.onGround = true;
            }
        }else if(mode.getMode().equalsIgnoreCase("Simple") && event instanceof EventPacketReceive){
            EventPacketReceive e = (EventPacketReceive) event;

            if(e.getPacket() instanceof S12PacketEntityVelocity){
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.getPacket();

                if(packet.getEntityID() == mc.thePlayer.getEntityId()){
                    if(this.horizontal.getValue() == 0.0 && this.vertical.getValue() == 0.0){
                        event.setCancelled(true);
                        return;
                    }

                    if(this.horizontal.getValue() == 100 && vertical.getValue() == 100){

                        return;
                    }

                    packet.motionX = (int) (horizontal.getValue() * packet.getMotionX() / 100);
                    packet.motionY = (int) (vertical.getValue() * packet.getMotionY() / 100);
                    packet.motionZ = (int) (horizontal.getValue() * packet.getMotionX() / 100);

                    e.updatePacket(packet);
                }

            }
        }
    }
}
