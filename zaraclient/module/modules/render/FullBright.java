package zaraclient.module.modules.render;

import org.lwjgl.input.Keyboard;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.ModeSetting;

public class FullBright extends Module {

    public ModeSetting modeSetting = new ModeSetting("Mode", "Gamma", "Gamma");

    public FullBright(){
        super("FullBright", Keyboard.KEY_NONE, Category.RENDER);
    }

    public float savedGamma;

    public void onEnable(){
        this.savedGamma = mc.gameSettings.gammaSetting;

        mc.gameSettings.gammaSetting = 100f;
    }

    public void onDisable(){
        mc.gameSettings.gammaSetting = this.savedGamma;
    }
}
