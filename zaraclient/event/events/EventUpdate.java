package zaraclient.event.events;

import zaraclient.event.Event;

public class EventUpdate extends Event {

    public double xPos, yPos, zPos;

    public float yaw, pitch;

    public boolean onGround;

    public double motionX, motionY, motionZ;

    public double getMotionX(){
        return motionX;
    }

    public double getMotionY(){
        return motionY;
    }

    public double getMotionZ(){
        return motionZ;
    }

    public double getX(){
        return xPos;
    }

    public double getY(){
        return yPos;
    }

    public double getZ(){
        return zPos;
    }

    public float getYaw(){
        return yaw;
    }

    public float getPitch(){
        return pitch;
    }

    public boolean isOnGround(){
        return onGround;
    }

    public EventUpdate(double x, double y, double z,  float yaw, float pitch, boolean onGround){
        this.xPos = x;
        this.yPos = y;
        this.zPos = z;

        this.yaw = yaw;
        this.pitch = pitch;

        this.onGround = onGround;

    }



}
