package zaraclient.event.events;

import zaraclient.event.Event;

public class EventStrafe extends Event {
    public float yaw;
    public float strafe;
    public float forward;
    public float friction;

    public float getYaw(){
        return this.yaw;
    }

    public EventStrafe(float yaw, float strafe, float forward, float friction){
        this.yaw = yaw;
        this.strafe = strafe;
        this.forward = forward;

        this.friction = friction;
    }

}
