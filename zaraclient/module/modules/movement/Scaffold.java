package zaraclient.module.modules.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import zaraclient.RandomUtils;
import zaraclient.event.Event;
import zaraclient.event.events.EventMotion;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.Timer;

public class Scaffold extends Module {

    public Scaffold(){
        super("Scaffold", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    public boolean rotated;
    public EnumFacing currentFacing;
    public BlockPos currentPos;

    public Timer timer = new Timer();

    public void onEvent(Event event) {
        if(event instanceof EventMotion){
            if(event.isPre()){

                BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

                getCurrentFacing(pos);

                if(this.currentPos != null && mc.theWorld.getBlockState(pos).getBlock() == Blocks.air && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock){
                    float yaw = getRotations(pos)[0];
                    float pitch = getRotations(pos)[1];

                    EventMotion motion = (EventMotion) event;

                    motion.yaw = yaw;
                    motion.pitch = pitch;


                    if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), currentPos, currentFacing, new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()))){
                        mc.thePlayer.swingItem();
                    }


                }
            }
        }
    }

    public float[] getRotations(BlockPos pos){
        double xDiff = pos.getX() - mc.thePlayer.posX;
        double zDiff = pos.getZ() - mc.thePlayer.posZ;
        double yDiff = pos.getY() - mc.thePlayer.posY - 1.2;

        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
        return new float[]{yaw, pitch};

    }



    public void getCurrentFacing(BlockPos var1) {
        if (mc.theWorld.getBlockState(var1.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, -1, 0);
            currentFacing = EnumFacing.UP;
        } else if (mc.theWorld.getBlockState(var1.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(-1, 0, 0);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(1, 0, 0);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, 0, -1);
            currentFacing = EnumFacing.SOUTH;
        } else if (mc.theWorld.getBlockState(var1.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, 0, 1);
            currentFacing = EnumFacing.NORTH;
        } else {
            currentFacing = null;
            currentPos = null;
        }
    }

}
