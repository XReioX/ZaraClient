package zaraclient.event.events;

import net.minecraft.network.Packet;
import zaraclient.event.Event;

public class EventPacketSend extends Event {

    public Packet packet;

    public EventPacketSend(Packet packet){
        this.packet = packet;
    }

    public void updatePacket(Packet packet){
        this.packet = packet;
    }

    public Packet getPacket(){
        return packet;
    }
}
