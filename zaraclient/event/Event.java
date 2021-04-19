package zaraclient.event;

import zaraclient.ZaraClient;
import zaraclient.module.Module;

public class Event {

    public EventType eventType;
    public boolean cancelled = false;

    public boolean isCancelled(){
        return cancelled;
    }

    public void setCancelled(boolean b){
        this.cancelled = b;
    }

    public void setType(EventType type){
        this.eventType = type;
    }

    public boolean isPre(){
        if(eventType == null){
            return false;
        }

        return eventType == EventType.PRE;
    }

    public boolean isPost(){
        if(eventType == null){
            return false;
        }

        return eventType == EventType.POST;
    }

    public void call(){
        for(Module module : ZaraClient.moduleManager.getModules()){
            if(module.isToggled()){
                module.onEvent(this);
            }
        }

        ZaraClient.onEvent(this);
    }
}
