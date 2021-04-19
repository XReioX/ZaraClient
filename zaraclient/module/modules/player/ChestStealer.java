package zaraclient.module.modules.player;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;
import zaraclient.ZaraClient;
import zaraclient.event.Event;
import zaraclient.event.events.EventMotion;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.NumberSetting;

public class ChestStealer extends Module {

    public NumberSetting delay = new NumberSetting("Delay (ms)", 200, 10, 1000, 5);

    public ChestStealer(){
        super("ChestStealer", Keyboard.KEY_NONE, Category.PLAYER);
    }

    public void onEvent(Event e) {
        if(e instanceof EventMotion && e.isPre()){
            if(mc.currentScreen != null && mc.currentScreen instanceof GuiChest){
                GuiChest chest = (GuiChest) mc.currentScreen;
                if(ZaraClient.moduleManager.getModuleByName("ChestStealer").isToggled()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for(int i = 0; i < chest.inventoryRows * 9; i++){
                                    Slot slot = (Slot) chest.inventorySlots.inventorySlots.get(i);

                                    if(slot.getStack() != null && !slot.getStack().getDisplayName().contains("skywars") && !slot.getStack().getDisplayName().contains("join")){

                                        Thread.sleep((long)delay.getValue());
                                        chest.handleMouseClick(slot, slot.slotNumber, 0, 1);
                                    }
                                }

                                mc.thePlayer.closeScreen();
                            }catch(Exception e){

                            }
                        }
                    }).start();
                }
            }
        }
    }
}
