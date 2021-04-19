package zaraclient.module.modules.misc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;
import zaraclient.MathUtils;
import zaraclient.MoveUtil;
import zaraclient.RandomUtils;
import zaraclient.event.Event;
import zaraclient.event.events.EventPacketReceive;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.Timer;
import zaraclient.module.setting.ModeSetting;
import zaraclient.module.setting.NumberSetting;
import zaraclient.event.events.EventPacketSend;
import zaraclient.notifications.Notification;
import zaraclient.notifications.NotificationManager;
import zaraclient.notifications.NotificationType;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Disabler extends Module {

    public ModeSetting modeSetting = new ModeSetting("Mode", "VerusCombat","MineplexCombat", "Lunar", "VerusCombat", "PingSpoof", "Hypixel5s", "Timer");

    public NumberSetting timerSpeed = new NumberSetting("Timer", 2, 1, 20, 0.1);
    public Disabler(){

        super("Disabler", Keyboard.KEY_NONE, Category.MISC);



        this.toggled = false;

        this.suffix = modeSetting.getMode();

        this.addSettings(modeSetting, timerSpeed);
    }

    public void onChanged(){
        this.suffix = modeSetting.getMode();
    }

    private List packetList = new CopyOnWriteArrayList();



    public boolean watchdog = false;

    public void onDisable(){
        if(modeSetting.getMode().equalsIgnoreCase("Lunar")){
            transactions.clear();
            currentTransaction = 0;
        }

        if(mc.timer.timerSpeed == timerSpeed.getValue()){
            mc.timer.timerSpeed = 1F;
        }
    }
    public void onEnable(){
        if(this.modeSetting.getMode().equalsIgnoreCase("Hypixel5s")){
            if(MoveUtil.isOnGround(0.001) && mc.thePlayer.isCollidedVertically){
                double x = mc.thePlayer.posX;
                double y = mc.thePlayer.posY;
                double z = mc.thePlayer.posZ;

                mc.thePlayer.motionY = 0.21;
                watchdog = true;
                NotificationManager.show(new Notification("Disabler", "Wait 5s", 5, NotificationType.INFO));
            }else{
                watchdog = false;
            }
        }else if(this.modeSetting.getMode().equalsIgnoreCase("Lunar")){
            mc.thePlayer.ticksExisted = 0;
        }
    }


    public Timer timer = new Timer();

    ArrayList<Packet> transactions = new ArrayList<Packet>();
    int currentTransaction = 0;

    public void onEvent(Event event){

        if(this.modeSetting.getMode().equalsIgnoreCase("Timer") && event instanceof EventUpdate) {
            mc.timer.timerSpeed = (float) this.timerSpeed.getValue();
        }else if(this.modeSetting.getMode().equalsIgnoreCase("Lunar") && event instanceof EventUpdate) {
            if(mc.thePlayer.ticksExisted % 120 == 0 && transactions.size() > currentTransaction) {
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(transactions.get(currentTransaction++));
            }
            if(mc.thePlayer.ticksExisted % 25 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomUtils.randomDouble(100, 1000),
                        mc.thePlayer.posZ, mc.thePlayer.onGround));
            }
            if(mc.thePlayer.ticksExisted % 600 == 0) {
                transactions.clear();
                currentTransaction = 0;
            }

        }else if(this.modeSetting.getMode().equalsIgnoreCase("Lunar") && event instanceof EventPacketSend) {
            EventPacketSend e = (EventPacketSend) event;

            if(e.getPacket() instanceof C17PacketCustomPayload) {
                C17PacketCustomPayload packet = (C17PacketCustomPayload) e.getPacket();

                if (packet.getChannelName().equalsIgnoreCase("MC|Brand")) {
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    ByteBuf message = Unpooled.buffer();
                    message.writeBytes("Lunar-Client".getBytes());
                    mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("REGISTER", new PacketBuffer(message)));
                }
            }

            if(e.getPacket() instanceof C0FPacketConfirmTransaction){
                transactions.add(e.getPacket());
                event.setCancelled(true);
            }

            if(e.getPacket() instanceof C00PacketKeepAlive){
                C00PacketKeepAlive packet = (C00PacketKeepAlive) e.getPacket();
                packet.key -= RandomUtils.random(1, 2147483647);
                e.updatePacket(packet);
            }

            if(e.getPacket() instanceof C03PacketPlayer){
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0CPacketInput());
            }

        }else if(this.modeSetting.getMode().equalsIgnoreCase("PingSpoof") && event instanceof EventPacketSend) {
            if (((EventPacketSend) event).getPacket() instanceof C00PacketKeepAlive && mc.thePlayer.isEntityAlive()) {
                this.packetList.add(((EventPacketSend) event).getPacket());

                event.setCancelled(true);
            }

            if (timer.hasTimeElapsed((long) (1000 * 50), true)) {
                if (!packetList.isEmpty()) {
                    int i = 0;
                    double totalPackets = MathUtils.getIncremental(Math.random() * 10.0D, 1.0D);
                    Iterator<Packet> var6 = this.packetList.iterator();
                    while (var6.hasNext()) {
                        Packet packet = var6.next();
                        if (i < totalPackets) {
                            i++;
                            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
                            this.packetList.remove(packet);
                        }
                    }

                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent((Packet) new C00PacketKeepAlive(10000));
                    this.timer.reset();
                }
            }
        }else if(modeSetting.getMode().equalsIgnoreCase("VerusCombat") && event instanceof EventPacketSend) {
            if (((EventPacketSend) event).getPacket() instanceof C0FPacketConfirmTransaction) {
                event.setCancelled(true);
            }
        }else if(modeSetting.getMode().equalsIgnoreCase("MineplexCombat") && event instanceof EventPacketSend){
            if(((EventPacketSend)event).getPacket() instanceof C00PacketKeepAlive){
                C00PacketKeepAlive packet = (C00PacketKeepAlive) ((EventPacketSend) event).getPacket();

                packet.key -= RandomUtils.random(1000, 2147483647);

                ((EventPacketSend) event).updatePacket(packet);
            }
        }else if(modeSetting.getMode().equalsIgnoreCase("Hypixel5s")){
            if(event instanceof EventPacketSend && event.isPre()){
                if(((EventPacketSend)event).getPacket() instanceof C03PacketPlayer){
                    if(watchdog && mc.thePlayer.motionY <= 0){
                        event.setCancelled(true);
                    }
                }
            }else if(event instanceof EventPacketReceive && event.isPre()){
                if(((EventPacketReceive)event).getPacket() instanceof S08PacketPlayerPosLook){
                    if(watchdog){
                        toggle();
                        NotificationManager.show(new Notification("Disabler", "Do whatever u want for 5s", 3, NotificationType.INFO));
                    }
                }
            }else if(event instanceof EventUpdate && event.isPre()){
                if (!watchdog) {
                    if (MoveUtil.isOnGround(0.001) && mc.thePlayer.isCollidedVertically) {
                        mc.thePlayer.motionY = 0.4;
                        watchdog = true;
                        NotificationManager.show(new Notification("Disabler", "Wait 5s", 3, NotificationType.WARNING));
                        //mc.thePlayer.jump();
                    }
                } else if (mc.thePlayer.motionY <= 0) {
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.motionZ = 0;
                    mc.thePlayer.jumpMovementFactor = 0;
                    mc.thePlayer.noClip = true;
                    mc.thePlayer.onGround = false;
                }
            }
        }
    }



}
