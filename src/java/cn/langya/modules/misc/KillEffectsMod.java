package cn.langya.modules.misc;

import java.util.ArrayList;

import cn.imflowow.LoadWorldEvent;
import net.minecraft.block.Block;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.update.UpdateEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;

public class KillEffectsMod implements Access.InstanceAccess {

	private final ComboValue modeV = new ComboValue("模式","雷","云","血","火","雷");
	private final NumberValue multiplierV = new NumberValue("数量",1,1,10,1);
	private final BooleanValue soundV = new BooleanValue("音效",true);
	private EntityLivingBase target;
    private int entityID;
	
	@EventTarget
	public void onUpdate(UpdateEvent event) {
        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null) {
            if (mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
                target = (EntityLivingBase) mc.objectMouseOver.entityHit;
            }
        }

        String mode = modeV.getValue();
		boolean sound = soundV.getValue();
		int multiplier = multiplierV.getValue().intValue();
		
		if (target != null && !mc.theWorld.loadedEntityList.contains(target) && mc.thePlayer.getDistanceSq(target.posX, mc.thePlayer.posY, target.posZ) < 100) {
			if (mc.thePlayer.ticksExisted > 3) {
				switch(mode) {
					case "雷":
                        final EntityLightningBolt entityLightningBolt = new EntityLightningBolt(mc.theWorld, target.posX, target.posY, target.posZ);
                        for (int i = 0; i < multiplier; i++) {
                            mc.theWorld.addEntityToWorld(entityID--, entityLightningBolt);
                        }

                        if (sound) {
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("ambient.weather.thunder"), ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        }
						break;
					case "火":
                        for (int i = 0; i < multiplier; i++) {
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.FLAME);
                        }
                        
                        if (sound) {
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("item.fireCharge.use"), ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        }
						break;
					case "云":
                        for (int i = 0; i < multiplier; i++) {
                            mc.effectRenderer.emitParticleAtEntity(target, EnumParticleTypes.CLOUD);
                        }
                        
                        if (sound) {
                            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("fireworks.twinkle"), ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        }
						break;
					case "血":

                        for (int i1 = 0; i1 < multiplier; i1++) {
                            for (int i = 0; i < 50; i++) {
                                mc.theWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK, target.posX, target.posY + target.height - 0.75, target.posZ, 0, 0, 0, Block.getStateId(Blocks.redstone_block.getDefaultState()));
                            }
                        }

                        if (sound) {
                            mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation("dig.stone"), 4.0F, 1.2F, ((float) target.posX), ((float) target.posY), ((float) target.posZ)));
                        }
						break;
				}
			}
			target = null;
		}
	}
	
	@EventTarget
	public void onLoadWorld(LoadWorldEvent event) {
		entityID = 0;
	}
}