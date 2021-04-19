package zaraclient;

import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class CombatUtil {

    public static List<Entity> ignoredEntities = new ArrayList<>();

    public static boolean isIgnoredEntity(Entity entity){
        return ignoredEntities.contains(entity);
    }

    public static void setAsIgnoredEntity(Entity entity, boolean should){
        if(should == false && isIgnoredEntity(entity)){
            ignoredEntities.remove(entity);
        }else if(should == true && !isIgnoredEntity(entity)){
            ignoredEntities.add(entity);
        }
    }

    public static void reset(){
        ignoredEntities.clear();
    }
}
