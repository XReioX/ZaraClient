package zaraclient.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class Notification {

    String title, text;

    public long end;
    public long start;
    public long fadedIn, fadeOut;

    public NotificationType type;


    public Notification(String title, String text, int length, NotificationType type){
        this.title = title;
        this.text = text;


        fadedIn = 200 * length;
        fadeOut = fadedIn + 500 * length;

        end = fadedIn + fadeOut;

        this.type = type;
    }

    public void show(){
        start = System.currentTimeMillis();
    }

    public boolean isShown(){
        return System.currentTimeMillis() - start <= end;
    }

    public FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

    public void render(){

        double offset = 0;
        int width = fr.getStringWidth(text) + 16;
        int height = 30;
        long time = System.currentTimeMillis() - start;

        if (time < fadedIn) {
            offset = Math.tanh(time / (double) (fadedIn) * 3.0) * width;
        } else if (time > fadeOut) {
            offset = (Math.tanh(3.0 - (time - fadeOut) / (double) ((fadedIn + fadeOut) - fadeOut) * 3.0) * width);
        } else {
            offset = width;
        }

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());




        Gui.drawRect(GuiScreen.width - offset, GuiScreen.height - 5 - height, GuiScreen.width, GuiScreen.height - 5, 0x90000000);
        Gui.drawRect(GuiScreen.width - offset, GuiScreen.height - 5 - height, GuiScreen.width - offset + 4, GuiScreen.height - 5, this.type == NotificationType.WARNING ? new Color(255,0,0).getRGB() : -1);

        fr.drawString(title, (int) (GuiScreen.width - offset + 8), GuiScreen.height - 2 - height, -1);
        fr.drawString(text, (int) (GuiScreen.width - offset + 8), GuiScreen.height - 15, -1);
    }
}
