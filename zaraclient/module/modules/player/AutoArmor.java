package zaraclient.module.modules.player;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.lwjgl.input.Keyboard;
import zaraclient.event.Event;
import zaraclient.event.events.EventMotion;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.Timer;
import zaraclient.module.setting.NumberSetting;

public class AutoArmor extends Module {

    public NumberSetting delay = new NumberSetting("Delay (ms)", 500, 10, 1000, 10);

    public AutoArmor(){
        super("AutoArmor", Keyboard.KEY_NONE, Category.PLAYER);

        this.addSettings(delay);
    }

    public Timer timer = new Timer();

    public void onEvent(Event event){
        if(event instanceof EventUpdate && event.isPre()){
            if(mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat){
                findBestArmor();
            }
        }
    }

    public void shiftClick(int slot){
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
    }

    public void drop(int slot){
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
    }


    public void findBestArmor(){
        for(int type = 1; type < 5; type++){
            if(mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()){
                ItemStack item = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if(isBestArmor(item, type)){
                    continue;
                }else{
                    if (!(mc.currentScreen instanceof GuiInventory)) {
                        C16PacketClientStatus p = new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT);
                        mc.thePlayer.sendQueue.addToSendQueue(p);
                        timer.reset();
                    }
                    drop(4 + type);
                }
            }
            for (int i = 9; i < 45; i++) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if(isBestArmor(is, type) && getProtection(is) > 0){
                        shiftClick(i);
                    }
                }
            }
        }
    }
    public boolean isBestArmor(ItemStack stack, int type){
        float prot = getProtection(stack);
        String strType = "";
        if(type == 1){
            strType = "helmet";
        }else if(type == 2){
            strType = "chestplate";
        }else if(type == 3){
            strType = "leggings";
        }else if(type == 4){
            strType = "boots";
        }
        if(!stack.getUnlocalizedName().contains(strType)){
            return false;
        }
        for (int i = 5; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
                    return false;
            }
        }
        return true;
    }

    public float getProtection(ItemStack stack){
        float prot = 0;
        if ((stack.getItem() instanceof ItemArmor)) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)/100d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)/100d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack)/100d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)/50d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)/100d;
        }
        return prot;
    }


}
