package zaraclient.module.modules.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import zaraclient.event.Event;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;

public class EagleWalk extends Module {

    public EagleWalk(){
        super("EagleWalk", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    public void onEvent(Event e){
        if(e instanceof EventUpdate && e.isPre()){
            boolean shouldSneak = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() == Blocks.air;

            if(shouldSneak){
                mc.gameSettings.keyBindSneak.pressed = true;
            }else{
                mc.gameSettings.keyBindSneak.pressed = false;
            }
        }
    }
}
