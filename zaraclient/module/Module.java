package zaraclient.module;

import net.minecraft.client.Minecraft;
import zaraclient.event.Event;
import zaraclient.module.setting.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {

    public String name;
    public String suffix = "";

    public boolean toggled;

    public int keybind;
    public Category category;

    public Minecraft mc = Minecraft.getMinecraft();

    public List<Setting> settings = new ArrayList<Setting>();

    public void addSettings(Setting... settings){
        this.settings.addAll(Arrays.asList(settings));
    }

    public boolean visible = true;

    public Module(String name, int keybind, Category category){
        this.name = name;
        this.keybind = keybind;
        this.category = category;
    }

    public String getName(){
        return this.name;
    }

    public String getSuffix(){
        return this.suffix;
    }

    public int getKeybind(){
        return this.keybind;
    }
    public int getKeyCode() { return this.keybind; }

    public boolean hasSuffix(){
        return this.suffix != "";
    }

    public void setKeyCode(int k){
        this.keybind = k;
    }

    public boolean isToggled(){
        return this.toggled;
    }

    public void enable(){
        if(!this.toggled){
            this.toggled = true;
            onEnable();
        }
    }

    public void disable(){
        if(this.toggled){
            this.toggled = false;
            onDisable();
        }
    }

    public void onEnable(){

    }

    public void onChanged(){

    }

    public void onDisable(){

    }

    public void onEvent(Event event){

    }

    public void setToggled(boolean b){
        this.toggled = b;

        if(this.toggled){
            onEnable();
        }else{
            onDisable();
        }
    }

    public void toggle(){
        this.toggled = !this.toggled;

        if(this.toggled){
            onEnable();
        }else{
            onDisable();
        }
    }

}
