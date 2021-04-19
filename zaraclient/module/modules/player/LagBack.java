package zaraclient.module.modules.player;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;
import zaraclient.ZaraClient;
import zaraclient.event.Event;
import zaraclient.event.events.EventPacketReceive;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.ModeSetting;
import zaraclient.notifications.Notification;
import zaraclient.notifications.NotificationManager;
import zaraclient.notifications.NotificationType;

public class LagBack extends Module {

    public ModeSetting modeSetting = new ModeSetting("Mode", "Combat", "Combat", "Movement", "All");

    public LagBack(){
        super("LagBack", Keyboard.KEY_NONE, Category.MISC);

        this.addSettings(modeSetting);

        this.suffix = modeSetting.getMode();
    }

    public void onChanged(){
        this.suffix = modeSetting.getMode();
    }

    public void onEvent(Event e){
        if(e instanceof EventPacketReceive){
            EventPacketReceive event = (EventPacketReceive) e;

            if(event.getPacket() instanceof S08PacketPlayerPosLook){
                NotificationManager.show(new Notification("LagBack", "LagBack detected..", 3, NotificationType.WARNING));

                if(this.modeSetting.getMode().equalsIgnoreCase("All")) {
                    ZaraClient.moduleManager.getModuleByName("KillAura").disable();
                    ZaraClient.moduleManager.getModuleByName("Speed").disable();
                    ZaraClient.moduleManager.getModuleByName("Scaffold").disable();
                    ZaraClient.moduleManager.getModuleByName("Fly").disable();
                    ZaraClient.moduleManager.getModuleByName("NoSwing").disable();

                }else if(this.modeSetting.getMode().equalsIgnoreCase("Movement")){
                    ZaraClient.moduleManager.getModuleByName("Scaffold").disable();
                    ZaraClient.moduleManager.getModuleByName("Speed").disable();
                    ZaraClient.moduleManager.getModuleByName("Fly").disable();
                }else if(this.modeSetting.getMode().equalsIgnoreCase("Combat")){
                    ZaraClient.moduleManager.getModuleByName("KillAura").disable();
                    ZaraClient.moduleManager.getModuleByName("NoSwing").disable();
                }

            }
        }
    }


}
