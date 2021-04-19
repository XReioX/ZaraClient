package zaraclient.module.modules.misc;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import zaraclient.event.Event;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;

public class AntiWeb extends Module {

    public AntiWeb(){
        super("AntiWeb", Keyboard.KEY_NONE, Category.MISC);
    }

    public void onEvent(Event event){
        if(event instanceof EventUpdate){

            if(mc.thePlayer.isInWeb){
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
                mc.thePlayer.isInWeb = false;

            }
        }
    }
}
