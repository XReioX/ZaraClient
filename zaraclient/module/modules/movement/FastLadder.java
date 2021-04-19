package zaraclient.module.modules.movement;

import org.lwjgl.input.Keyboard;
import zaraclient.event.Event;
import zaraclient.event.events.EventMotion;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;

public class FastLadder extends Module {

    public FastLadder(){
        super("FastLadder", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    public void onEvent(Event event){
        if(event instanceof EventMotion){
            if(mc.thePlayer.isOnLadder()){
                mc.thePlayer.motionY = 0.15;
                ((EventMotion)event).onGround = true;
            }
        }
    }
}
