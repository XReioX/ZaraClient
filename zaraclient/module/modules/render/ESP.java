package zaraclient.module.modules.render;

import javafx.scene.paint.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;
import zaraclient.RenderUtils;
import zaraclient.event.Event;
import zaraclient.event.events.Event3D;
import zaraclient.event.events.EventUpdate;
import zaraclient.module.Category;
import zaraclient.module.Module;
import zaraclient.module.setting.BooleanSetting;
import zaraclient.module.setting.ModeSetting;
import zaraclient.module.setting.NumberSetting;


public class ESP extends Module {

    public ModeSetting targeting = new ModeSetting("Mode", "Everyone", "Everyone", "Players", "Mobs", "Animals");
    public BooleanSetting isOutline = new BooleanSetting("Outline only", true);
    public BooleanSetting accountDistance = new BooleanSetting("Account Distance", false);

    public NumberSetting red = new NumberSetting("Red", 50, 0, 255, 1);
    public NumberSetting green = new NumberSetting("Green", 50, 0, 255, 1);
    public NumberSetting blue = new NumberSetting("Blue", 50, 0, 255, 1);

    public ESP(){
        super("ESP", Keyboard.KEY_NONE, Category.RENDER);

        this.addSettings(targeting, accountDistance, isOutline, red, green, blue);
    }

    public void onEvent(Event event){
        if(event instanceof Event3D){
            for(Entity entity : mc.theWorld.loadedEntityList){
                if(entity != null && entity instanceof EntityLivingBase && entity != mc.thePlayer){
                    if(targeting.getMode().equalsIgnoreCase("Everyone")){
                        render((EntityLivingBase)entity, ((Event3D) event).partialTicks);
                    }else if(targeting.getMode().equalsIgnoreCase("Players") && entity instanceof EntityPlayer){
                        render((EntityLivingBase)entity, ((Event3D) event).partialTicks);
                    }else if(targeting.getMode().equalsIgnoreCase("Mobs") && entity instanceof EntityMob){
                        render((EntityLivingBase)entity, ((Event3D) event).partialTicks);
                    }else if(targeting.getMode().equalsIgnoreCase("Animals") && entity instanceof EntityAnimal){
                        render((EntityLivingBase)entity, ((Event3D) event).partialTicks);
                    }
                }
            }
        }
    }

    public void render(EntityLivingBase e, float partialTicks){
        if(e != null && e instanceof EntityLivingBase && e.ticksExisted > 10){
            double xPos = (e.lastTickPosX + (e.posX - e.lastTickPosX) * mc.timer.renderPartialTicks)
                    - mc.getRenderManager().renderPosX;
            double yPos = (e.lastTickPosY + (e.posY - e.lastTickPosY) * mc.timer.renderPartialTicks)
                    - mc.getRenderManager().renderPosY;
            double zPos = (e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * mc.timer.renderPartialTicks)
                    - mc.getRenderManager().renderPosZ;






            if(isOutline.getBoolean()){
                RenderUtils.drawEntityBox(e, (float)red.getValue(), (float)green.getValue(), (float)blue.getValue(), true);
            }else{
                RenderUtils.drawEntityBox(e, (float)red.getValue(), (float)green.getValue(), (float)blue.getValue(), false);
            }
        }
    }

}
