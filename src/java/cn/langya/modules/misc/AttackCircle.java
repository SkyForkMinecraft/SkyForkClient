package cn.langya.modules.misc;

import cn.cedo.misc.ColorUtil;
import cn.cedo.render.RenderUtil;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render3DEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;

import java.awt.*;

public class AttackCircle implements Access.InstanceAccess {

    private final BooleanValue targetCircle = new BooleanValue("敌对光环",true);
    private static final NumberValue targetMaxDistance = new NumberValue("敌对最大距离",3,3,8,1);
    private final BooleanValue playerCircle = new BooleanValue("玩家光环",true);
    private final NumberValue circleAccuracy = new NumberValue("圆角值", 15, 0, 60,2);
    private static final ComboValue colorMode = new ComboValue("颜色模式","客户端","客户端","自定义","彩虹");
    private static final NumberValue customColorRed = new NumberValue("自定义红色",0,0,255,5);
    private static final NumberValue customColorGreen = new NumberValue("自定义绿色",0,0,255,5);
    private static final NumberValue customColorBlue = new NumberValue("自定义蓝色",0,0,255,5);

    @EventTarget
    void onRender3D(Render3DEvent e) {
        float range = 3.0F;

        if (playerCircle.getValue()) {
            drawCircle(range, mc.thePlayer);
        }

        if (targetCircle.getValue()) {
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity != mc.thePlayer && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() > 0 && !entity.isDead && mc.thePlayer.getDistanceToEntity(entity) < targetMaxDistance.getValue()) {
                    drawCircle(range, (EntityLivingBase) entity);
                }
            }
        }

    }

    private void drawCircle(float range,EntityLivingBase target) {
        if (target.isDead || target.getHealth() <= 0 || target.getDistanceToEntity(mc.thePlayer) > targetMaxDistance.getValue()) return;
        GL11.glPushMatrix();
        GL11.glTranslated(
                target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX,
                target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY,
                target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ
        );
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Color color = null;
        switch (colorMode.getValue()) {
            case "自定义" : color = new Color(customColorRed.getValue().intValue(), customColorGreen.getValue().intValue(), customColorBlue.getValue().intValue()); break;
            case "客户端": color = Access.CLIENT_COLOR; break;
            case "彩虹": color = new Color(ColorUtil.getColor(-(1 + 5 * 1.7f), 0.7f, 1));
        }

        GL11.glLineWidth(1F);
        RenderUtil.resetColor(color);
        GL11.glRotatef(90F, 1F, 0F, 0F);
        GL11.glBegin(GL11.GL_LINE_STRIP);

        for (int i = 0; i <= 360; i += 61 - circleAccuracy.getValue().intValue()) { // You can change circle accuracy (60 - accuracy)
            GL11.glVertex2f((float) Math.cos(i * Math.PI / 180.0) * range, (float) Math.sin(i * Math.PI / 180.0) * range);
        }
        GL11.glVertex2f((float) Math.cos(360 * Math.PI / 180.0) * range, (float) Math.sin(360 * Math.PI / 180.0) * range);

        GL11.glEnd();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GL11.glPopMatrix();
    }

}
