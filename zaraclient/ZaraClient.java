package zaraclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import zaraclient.event.Event;
import zaraclient.event.events.Event2D;
import zaraclient.event.events.EventKey;
import zaraclient.module.Module;
import zaraclient.module.ModuleManager;
import zaraclient.module.modules.combat.AntiBot;
import zaraclient.module.modules.movement.Fly;
import zaraclient.notifications.NotificationManager;

import java.util.Random;
import java.util.UUID;

public class ZaraClient {

    public static ModuleManager moduleManager = new ModuleManager();

    public static boolean hooked;

    public static void hook(){
        hooked = true;
        Display.setTitle("[ZaraClient] V6 (1.8.8)");

        moduleManager.addModules();
    }

    public static void sendChat(String message){
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

    public static String account = "jumpyjumpy123";
    public static String uuid = String.valueOf(UUID.randomUUID());

    public static void handleCommand(String command){
        if(command.equalsIgnoreCase("-resetnotifications")){
            NotificationManager.reset();
            sendChat(EnumChatFormatting.GREEN + "Success!");
        }else if(command.equalsIgnoreCase("-fly hypixel")){
            Fly fly = (Fly) moduleManager.getModuleByName("Fly");

            fly.modeSetting.setMode("ZoomFly");
            fly.speedSetting.setValue(2);
            fly.setKeyCode(Keyboard.KEY_F);
            sendChat("Fly set to zoomfly, speed changed to 2, and keybind set to F.");

        }else if(command.equalsIgnoreCase("-resetbots")){
            CombatUtil.reset();
            sendChat(EnumChatFormatting.GREEN + "Success!");
        }else if(command.equalsIgnoreCase("-randomalt")){
            Session session = new Session(account, uuid, null, "mojang");
            Minecraft.getMinecraft().session = session;
            sendChat("you're logged in as " + account + " (rejoin is needed)");
        }
    }

    public static void unhook(){
        hooked = false;
        Display.setTitle("Minecraft 1.8.8");

        moduleManager.removeModules();
    }

    public static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

    public static Minecraft mc = Minecraft.getMinecraft();
    public static void onEvent(Event e){
        if(e instanceof EventKey){
            EventKey keyEvent = (EventKey) e;
            for(Module module : moduleManager.getModules()){
                if(module.getKeybind() == keyEvent.getKey()){
                    module.toggle();
                }
            }
        }else if(e instanceof Event2D){

            GuiInventory.drawEntityOnScreen(10 + (fr.getStringWidth("ZaraClient V6") / 2), 100, 40, 0, 0, Minecraft.getMinecraft().thePlayer);
            fr.drawStringWithShadow("ZaraClient V6", 10, 10, 0xff0090ff);

        }
    }


}
