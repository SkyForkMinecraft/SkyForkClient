//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Administrator\Downloads\Minecraft1.12.2 Mappings"!

//Decompiled by Procyon!

package cn.starx.mobends.client.renderer.entity;

import cn.starx.mobends.client.model.entity.ModelBendsPlayer;
import cn.starx.mobends.client.renderer.entity.layers.LayerBendsCustomHead;
import cn.starx.mobends.client.renderer.entity.layers.LayerBendsPlayerArmor;
import cn.starx.mobends.data.Data_Player;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import waveycapes.renderlayers.CustomCapeRenderLayer;

public class RenderBendsPlayer extends RenderPlayer
{
    private final boolean smallArms;
    
    public RenderBendsPlayer(final RenderManager renderManager) {
        super(renderManager, false);
        this.smallArms = false;
        this.mainModel = (ModelBase)new ModelBendsPlayer(0.0f, false);
        this.layerRenderers.clear();
        this.addLayer((LayerRenderer)new LayerBendsPlayerArmor((RendererLivingEntity)this));
        this.addLayer((LayerRenderer)new LayerHeldItem((RendererLivingEntity)this));
        this.addLayer((LayerRenderer)new LayerBendsCustomHead((ModelBendsPlayer)this.getMainModel()));
        this.addLayer((LayerRenderer)new CustomCapeRenderLayer(this, (ModelBase)this.getMainModel()));
    }
    
    public RenderBendsPlayer(final RenderManager renderManager, final boolean useSmallArms) {
        super(renderManager, useSmallArms);
        this.smallArms = useSmallArms;
        this.mainModel = (ModelBase)new ModelBendsPlayer(0.0f, useSmallArms);
        this.layerRenderers.clear();
        this.addLayer((LayerRenderer)new LayerBendsPlayerArmor((RendererLivingEntity)this));
        this.addLayer((LayerRenderer)new LayerHeldItem((RendererLivingEntity)this));
        this.addLayer((LayerRenderer)new LayerBendsCustomHead((ModelBendsPlayer)this.getMainModel()));
        this.addLayer((LayerRenderer)new CustomCapeRenderLayer(this, (ModelBase)this.getMainModel()));
    }
    
    public ModelPlayer getMainModel() {
        if (!(this.mainModel instanceof ModelBendsPlayer)) {
            this.mainModel = (ModelBase)new ModelBendsPlayer(0.0f, this.smallArms);
        }
        return (ModelPlayer)this.mainModel;
    }
    
    private void setModelVisibilities(final AbstractClientPlayer clientPlayer) {
        final ModelBendsPlayer modelplayer = (ModelBendsPlayer)this.getMainModel();
        if (clientPlayer.isSpectator()) {
            modelplayer.setInvisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        }
        else {
            final ItemStack itemstack = clientPlayer.inventory.getCurrentItem();
            modelplayer.setInvisible(true);
            modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
            modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
            modelplayer.heldItemLeft = 0;
            modelplayer.aimedBow = false;
            modelplayer.isSneak = clientPlayer.isSneaking();
            if (itemstack == null) {
                modelplayer.heldItemRight = 0;
            }
            else {
                modelplayer.heldItemRight = 1;
                if (clientPlayer.getItemInUseCount() > 0) {
                    final EnumAction enumaction = itemstack.getItemUseAction();
                    if (enumaction == EnumAction.BLOCK) {
                        modelplayer.heldItemRight = 3;
                    }
                    else if (enumaction == EnumAction.BOW) {
                        modelplayer.aimedBow = true;
                    }
                }
            }
        }
    }
    
    protected ResourceLocation getEntityTexture(final AbstractClientPlayer entity) {
        return entity.getLocationSkin();
    }
    
    protected void preRenderCallback(final AbstractClientPlayer clientPlayer, final float partialTicks) {
        final float f1 = 0.9375f;
        GlStateManager.scale(f1, f1, f1);
        ((ModelBendsPlayer)this.getMainModel()).updateWithEntityData(clientPlayer);
        ((ModelBendsPlayer)this.mainModel).postRenderTranslate(0.0625f);
        final Data_Player data = Data_Player.get(clientPlayer.getEntityId());
        GL11.glPushMatrix();
        final float f2 = 0.0625f;
        GL11.glScalef(-f2, -f2, f2);
        data.swordTrail.render((ModelBendsPlayer)this.getMainModel());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
        ((ModelBendsPlayer)this.getMainModel()).postRenderRotate(0.0625f);
    }
    
    public void renderRightArm(final AbstractClientPlayer clientPlayer) {
        final float f = 1.0f;
        GlStateManager.color(f, f, f);
        final ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        modelplayer.swingProgress = 0.0f;
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, (Entity)clientPlayer);
        modelplayer.renderRightArm();
    }
    
    public void renderLeftArm(final AbstractClientPlayer clientPlayer) {
        final float f = 1.0f;
        GlStateManager.color(f, f, f);
        final ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(modelplayer.swingProgress = 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, (Entity)clientPlayer);
        modelplayer.renderLeftArm();
    }
    
    protected void renderLivingAt(final AbstractClientPlayer entityLivingBaseIn, final double x, final double y, final double z) {
        super.renderLivingAt(entityLivingBaseIn, x, y, z);
    }
}
