package zaraclient.event.events;

import zaraclient.event.Event;

public class EventKey extends Event {

    int keycode;

    public EventKey(int key){
        this.keycode = key;
    }

    public int getKey(){
        return keycode;
    }

    public void setKey(int key){
        this.keycode = key;
    }
}
