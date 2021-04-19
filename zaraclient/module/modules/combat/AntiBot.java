package zaraclient.module.modules.combat;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import zaraclient.CombatUtil;
import zaraclient.event.Event;
import zaraclient.event.events.EventPacketReceive;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.Timer;
import zaraclient.module.setting.BooleanSetting;
import zaraclient.module.setting.ModeSetting;
import zaraclient.notifications.Notification;
import zaraclient.notifications.NotificationManager;
import zaraclient.notifications.NotificationType;

import java.awt.*;

public class AntiBot extends Module {

    public ModeSetting modeSetting = new ModeSetting("Mode", "Hypixel", "Hypixel", "Advanced");
    public BooleanSetting invisibleCheck = new BooleanSetting("Invisible Check", true);
    public BooleanSetting autoResetBots = new BooleanSetting("Automatic bot resets", true);


    public AntiBot(){
        super("AntiBot", Keyboard.KEY_NONE, Category.COMBAT);

        this.addSettings(modeSetting, invisibleCheck, autoResetBots);
    }

    public Timer timer = new Timer();

    public void onEvent(Event event){
        if(event instanceof EventUpdate && autoResetBots.getBoolean() == true){
            if(timer.hasTimeElapsed(1500, true)){
                CombatUtil.reset();
                NotificationManager.show(new Notification("AntiBot", "All bots were reset.", 3, NotificationType.INFO));
            }
        }
        if(modeSetting.getMode().equalsIgnoreCase("Advanced") && event instanceof EventPacketReceive) {
            EventPacketReceive e = (EventPacketReceive) event;

            if (e.getPacket() instanceof S0CPacketSpawnPlayer) {
                S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) e.getPacket();

                double entityX = packet.getX() / 32D;
                double entityY = packet.getY() / 32D;
                double entityZ = packet.getZ() / 32D;

                double differenceX = mc.thePlayer.posX - entityX;
                double differenceY = mc.thePlayer.posY - entityY;
                double differenceZ = mc.thePlayer.posZ - entityZ;

                float dis = MathHelper.sqrt_double(differenceX * differenceX + differenceY * differenceY + differenceZ * differenceZ);

                if (dis <= 17 && entityY > mc.thePlayer.posY + 1 && (entityX != mc.thePlayer.posX && entityY != mc.thePlayer.posY && entityZ != mc.thePlayer.posZ)) {
                    NotificationManager.show(new Notification("AntiBot", "Bot removed (adv)", 3, NotificationType.INFO));
                    event.setCancelled(true);
                }
            }
        }

        if(modeSetting.getMode().equalsIgnoreCase("Hypixel") && event instanceof EventUpdate) {
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity != null && entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;

                    if (mc.getNetHandler().getPlayerInfo((player).getUniqueID()) == null && !CombatUtil.isIgnoredEntity(player)) {
                        NotificationManager.show(new Notification("AntiBot", "Bot called " + player.getName() + " removed (3)", 3, NotificationType.INFO));
                        CombatUtil.setAsIgnoredEntity(player, true);
                    }
                }
            }
        }

        if(modeSetting.getMode().equalsIgnoreCase("Hypixel") && event instanceof EventUpdate) {

            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity != null && entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;

                    if (!CombatUtil.isIgnoredEntity(player) && player.getGameProfile() == null) {
                        NotificationManager.show(new Notification("AntiBot", "Bot called " + player.getName() + " removed (2)", 3, NotificationType.INFO));
                        CombatUtil.setAsIgnoredEntity(player, true);
                    }
                }
            }
        }

        if(modeSetting.getMode().equalsIgnoreCase("Hypixel") && event instanceof EventUpdate && invisibleCheck.getBoolean() == true){
            for(Entity entity : mc.theWorld.loadedEntityList){
                if(entity != null && entity instanceof EntityPlayer){
                    EntityPlayer player = (EntityPlayer) entity;

                    if(player.isInvisibleToPlayer(mc.thePlayer) || player.isInvisible()){
                        if(!CombatUtil.isIgnoredEntity(player) && player != mc.thePlayer){
                            NotificationManager.show(new Notification("AntiBot", "Bot called " + player.getName() + " removed", 3, NotificationType.INFO));
                            CombatUtil.setAsIgnoredEntity(player, true);
                        }
                    }
                }
            }
        }
    }
}
