package zaraclient.module.modules.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import zaraclient.ZaraClient;
import zaraclient.event.Event;
import zaraclient.event.events.Event2D;
import zaraclient.module.Category;
import zaraclient.module.Module;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ModsList extends Module {

    public ModsList(){
        super("ArrayList", Keyboard.KEY_NONE, Category.RENDER);

        enable();
    }

    public FontRenderer fr = mc.fontRendererObj;

    public static int getRainbowWave(float seconds, float saturation, float brightness, long index){
        float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000)) / (float)(seconds * 1000);

        return Color.HSBtoRGB(hue, saturation, brightness);
    }


    public void onEvent(Event event){
        if(event instanceof Event2D){
            List<Module> modules = ZaraClient.moduleManager.getModules();
            modules.sort(new Comparator<Module>() {
                @Override
                public int compare(Module o1, Module o2) {
                    if(fr.getStringWidth(o1.name + " " + o1.getSuffix()) > fr.getStringWidth(o2.name + " " + o2.getSuffix())){
                        return -1;
                    }

                    if(fr.getStringWidth(o1.name + " " + o1.getSuffix()) < fr.getStringWidth(o2.name + " " + o2.getSuffix())){
                        return 1;
                    }

                    return 0;

                }
            });



            int count = 0;
            ScaledResolution sr = new ScaledResolution(mc);
            for(Module module : modules){
                if(!module.isToggled()) continue;

                double offset = count*(fr.FONT_HEIGHT + 6);
                Gui.drawRect(sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(module.getName() + module.getSuffix()) - 13, offset, sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(module.getName() + module.getSuffix()) - 10, 6 + fr.FONT_HEIGHT + offset, 0xff008080);
                Gui.drawRect(sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(module.getName() + module.getSuffix()) - 10, offset, sr.getScaledWidth(), 6 + fr.FONT_HEIGHT + offset, 0x90000000);
                fr.drawString(module.getName(), sr.getScaledWidth() - fr.getStringWidth(module.getName() + module.getSuffix()) - 7, 4 + offset, 0xff0090ff);
                fr.drawString(module.getSuffix(), sr.getScaledWidth() - fr.getStringWidth(module.getSuffix()) - 4, 4 + offset, 0xffB0B0B0);
                count++;
            }


        }
    }
}
