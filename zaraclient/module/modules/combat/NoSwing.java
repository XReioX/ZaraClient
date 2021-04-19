package zaraclient.module.modules.combat;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import org.lwjgl.input.Keyboard;
import zaraclient.event.Event;
import zaraclient.event.events.EventPacketSend;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.BooleanSetting;

public class NoSwing extends Module {

    public BooleanSetting clientSideOnly = new BooleanSetting("Client Side Only", true);

    public NoSwing(){
        super("NoSwing", Keyboard.KEY_NONE, Category.COMBAT);

        this.addSettings(clientSideOnly);
    }

    public void onEvent(Event e){
        if(clientSideOnly.getBoolean() == false){
            if(e instanceof EventPacketSend){
                EventPacketSend event = (EventPacketSend) e;

                if(event.getPacket() instanceof C0APacketAnimation){
                    event.setCancelled(true);
                }
            }
        }
    }
}
