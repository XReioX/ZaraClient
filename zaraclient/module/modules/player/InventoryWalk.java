package zaraclient.module.modules.player;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import zaraclient.event.Event;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;

public class InventoryWalk extends Module {

    public InventoryWalk(){
        super("InventoryWalk", Keyboard.KEY_NONE, Category.PLAYER);
    }

    public void onEvent(Event e){
        if(e instanceof EventUpdate){

            if(mc.currentScreen instanceof GuiChest){
                KeyBinding[] moveKeys = new KeyBinding[]{
                        mc.gameSettings.keyBindForward,
                        mc.gameSettings.keyBindBack,
                        mc.gameSettings.keyBindLeft,
                        mc.gameSettings.keyBindRight,
                        mc.gameSettings.keyBindJump
                };
                for (KeyBinding bind : moveKeys){
                    KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
                }
            }
        }
    }
}
