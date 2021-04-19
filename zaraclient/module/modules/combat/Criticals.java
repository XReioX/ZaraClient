package zaraclient.module.modules.combat;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import zaraclient.event.Event;
import zaraclient.event.events.EventPacketSend;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.ModeSetting;

public class Criticals extends Module {

    public ModeSetting modeSetting = new ModeSetting("Mode", "Packet", "Packet", "Jump");
    public Criticals(){
        super("Criticals", Keyboard.KEY_NONE, Category.COMBAT);

        this.addSettings(modeSetting);
    }

    public void onEvent(Event e){
        if(e instanceof EventPacketSend && modeSetting.getMode().equalsIgnoreCase("Packet")){
            EventPacketSend event = (EventPacketSend) e;

            if(event.getPacket() instanceof C02PacketUseEntity){
                C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();

                if(packet.getAction() == C02PacketUseEntity.Action.ATTACK){
                    double[] var5;
                    var5 = new double[]{0.051D, 0.011511D, 0.001D, 0.001D};

                    for(int i = 0; i < var5.length; i++){
                        double offset = var5[i];
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                    }




                }
            }
        }else if(e instanceof EventPacketSend && modeSetting.getMode().equalsIgnoreCase("Jump")){
            EventPacketSend event = (EventPacketSend) e;

            if(event.getPacket() instanceof C02PacketUseEntity){
                C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();

                if(packet.getAction() == C02PacketUseEntity.Action.ATTACK){
                    if(mc.thePlayer.onGround){
                        mc.thePlayer.jump();
                    }



                }
            }
        }
    }
}
