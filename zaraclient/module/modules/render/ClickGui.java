package zaraclient.module.modules.render;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import zaraclient.event.Event;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.ModeSetting;


public class ClickGui extends Module{

    public clickgui.ClickGui clickGui = new clickgui.ClickGui();

    public ClickGui(){
        super("ClickGui", Keyboard.KEY_K, Category.RENDER);
    }

    public void onEnable(){
        mc.displayGuiScreen(clickGui);
    }

}
