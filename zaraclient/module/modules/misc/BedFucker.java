package zaraclient.module.modules.misc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import zaraclient.ZaraClient;
import zaraclient.event.Event;
import zaraclient.event.events.EventMotion;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.BooleanSetting;

public class BedFucker extends Module {

    public BooleanSetting rotate = new BooleanSetting("AutoRotate", false);

    public BedFucker(){
        super("BedFucker", Keyboard.KEY_NONE, Category.MISC);

        this.addSettings(rotate);
    }

    public int size = 4;
    public int sizeOther = size/2;

    public BlockPos findBed(){
        for(int x = -size; x < size + sizeOther; x++){
            for(int z = -size; z < size + sizeOther; z++){
                for(int y = -size; y < size + sizeOther; y++){
                    int blockX = (int) (mc.thePlayer.posX + x);
                    int blockY = (int) (mc.thePlayer.posY + y);
                    int blockZ = (int) (mc.thePlayer.posZ + z);
                    if(mc.theWorld.getBlockState(new BlockPos(blockX, blockY, blockZ)).getBlock() instanceof BlockBed){
                        return new BlockPos(blockX, blockY, blockZ);
                    }
                }
            }
        }

        return null;
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

    public void onEvent(Event event){
        if(event instanceof EventMotion && event.isPost()){
            BlockPos bed = findBed();

            if(bed != null){
                if(!ZaraClient.moduleManager.getModuleByName("KillAura").isToggled() && rotate.getBoolean() == true){
                    ((EventMotion)event).yaw = getRotations(bed)[0];
                    ((EventMotion)event).pitch = getRotations(bed)[1];
                }
                mc.playerController.onPlayerDamageBlock(bed, EnumFacing.NORTH);
            }
        }
    }
}
