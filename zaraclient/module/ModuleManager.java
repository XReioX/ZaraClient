package zaraclient.module;

import zaraclient.module.modules.combat.*;
import zaraclient.module.modules.misc.AntiWeb;
import zaraclient.module.modules.misc.BedFucker;
import zaraclient.module.modules.misc.Disabler;
import zaraclient.module.modules.movement.*;
import zaraclient.module.modules.player.*;
import zaraclient.module.modules.render.ClickGui;
import zaraclient.module.modules.render.ESP;
import zaraclient.module.modules.render.FullBright;
import zaraclient.module.modules.render.ModsList;
import zaraclient.module.setting.Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ModuleManager {

    public CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();

    public void addModules(){
        modules.add(new KillAura());
        modules.add(new AntiBot());
        modules.add(new Fly());
        modules.add(new ModsList());
        modules.add(new NoFall());
        modules.add(new Disabler());
        modules.add(new Criticals());
        modules.add(new InventoryWalk());
        modules.add(new FullBright());
        modules.add(new NoSwing());
        modules.add(new LagBack());
        modules.add(new Aimbot());
        modules.add(new Speed());
        modules.add(new EagleWalk());
        modules.add(new Velocity());
        modules.add(new AntiWeb());
        modules.add(new Jesus());
        modules.add(new ChestStealer());
        modules.add(new AutoArmor());
        modules.add(new BedFucker());
        modules.add(new ESP());
        modules.add(new Scaffold());
        modules.add(new FastLadder());
        modules.add(new ClickGui());
    }

    public ArrayList<Setting> getSettingsByMod(Module mod){
        return new ArrayList<>(mod.settings);
    }

    public void removeModules(){
        this.disableAll();

        modules.clear();
    }

    public void disableAll(){
        modules.stream().filter(p -> p.toggled).collect(Collectors.toList()).forEach(Module::enable);
    }

    public void enableAll(){
        modules.stream().filter(p -> !p.toggled).collect(Collectors.toList()).forEach(Module::disable);
    }

    public List<Module> getModules(){
        return modules;
    }

    public Module getModuleByName(String name){
        if(modules.stream().filter(p -> p.name.equalsIgnoreCase(name)).findFirst().isPresent()){
            return modules.stream().filter(p -> p.name.equalsIgnoreCase(name)).findFirst().get();
        }

        return null;
    }

    public List<Module> getModulesByCategory(Category category){
        return modules.stream().filter(p -> p.category == category).collect(Collectors.toList());
    }
}
