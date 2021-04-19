package zaraclient.module.setting;

public class BooleanSetting extends Setting{

    public boolean aBoolean;
    public BooleanSetting(String name, boolean b){
        this.name = name;
        this.aBoolean = b;

    }

    public boolean getBoolean(){
        return aBoolean;
    }

    public void setBoolean(boolean b){
        this.aBoolean = b;
    }

}
