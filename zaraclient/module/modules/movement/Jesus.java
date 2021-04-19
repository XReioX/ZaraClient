package zaraclient.module.modules.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import zaraclient.event.Event;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.ModeSetting;

public class Jesus extends Module {

    public ModeSetting modeSetting = new ModeSetting("Mode", "Bypass", "Bypass", "Motion");

    public Jesus(){
        super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT);

        this.addSettings(modeSetting);

        this.suffix = modeSetting.getMode();
    }

    public void onChanged(){
        this.suffix = modeSetting.getMode();
    }

    public void onEvent(Event event){
        if(modeSetting.getMode().equalsIgnoreCase("Motion") && event instanceof EventUpdate){
            BlockPos otherPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
            if(mc.theWorld.getBlockState(otherPos).getBlock() instanceof BlockLiquid){
                mc.thePlayer.motionY = 0;
            }
            //KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);




        }else if(modeSetting.getMode().equalsIgnoreCase("Bypass") && event instanceof EventUpdate){
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
        }
    }
}
