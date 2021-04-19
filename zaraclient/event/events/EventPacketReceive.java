package zaraclient.event.events;

import net.minecraft.network.Packet;
import zaraclient.event.Event;

public class EventPacketReceive extends Event {

    public Packet packet;

    public EventPacketReceive(Packet packet){
        this.packet = packet;
    }

    public void updatePacket(Packet packet){
        this.packet = packet;
    }

    public Packet getPacket(){
        return packet;
    }
}
