package zaraclient.module.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting{

    public List<String> modes = new ArrayList<>();

    public int index;
    public ModeSetting(String name, String mode, String... modes){
        this.name = name;
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(mode);
    }

    public String getMode(){
        return modes.get(index);
    }

    public void cycle(){
        if(index < modes.size() - 1){
            index++;
        }else{
            index = 0;
        }
    }

    public void setMode(String string){
        this.index = modes.indexOf(string);
    }
}
