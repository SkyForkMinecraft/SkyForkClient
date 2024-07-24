package com.skyfork.api.langya.modules.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.render.Render3DEvent;
import com.skyfork.client.value.impl.NumberValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LangYa
 * @since 2024/06/27/下午8:02
 */
public class TargetCircle implements Access.InstanceAccess {

    private static final NumberValue targetMaxDistance = new NumberValue("敌对最大距离",3,3,8,1);
    private static final NumberValue customColorRed = new NumberValue("自定义红色",255,0,255,5);
    private static final NumberValue customColorGreen = new NumberValue("自定义绿色",255,0,255,5);
    private static final NumberValue customColorBlue = new NumberValue("自定义蓝色",255,0,255,5);

    @EventTarget
    void onRender3D(Render3DEvent e) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != mc.thePlayer && entity instanceof EntityPlayer && ((EntityPlayer) entity).getHealth() > 0 && !entity.isDead && mc.thePlayer.getDistanceToEntity(entity) < targetMaxDistance.getValue()) {
                doRender((EntityPlayer) entity);
            }
        }
    }

    public void doRender(EntityPlayer target) {
        long drawTime = System.currentTimeMillis() % 2000;
        boolean drawMode = drawTime > 1000;
        double drawPercent = drawTime / 1000.0;

        if (!drawMode) {
            drawPercent = 1.5 - drawPercent;
        } else {
            drawPercent -= 1.5;
        }

        drawPercent = easeInOutQuad(drawPercent);
        List<Vec3> points = new ArrayList<>();
        AxisAlignedBB bb = target.getEntityBoundingBox();
        double radius = bb.maxX - bb.minX;
        double height = bb.maxY - bb.minY;
        double posX = target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks;
        double posY = target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks;

        if (drawMode) {
            posY -= 0.5;
        } else {
            posY += 0.5;
        }

        double posZ = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks;

        for (int i = 0; i <= 360; i += 7) {
            points.add(new Vec3(posX - Math.sin(i * Math.PI / 180) * radius, posY + height * drawPercent, posZ + Math.cos(i * Math.PI / 180) * radius));
        }

        points.add(points.get(0)); // Closing the loop

        // Draw
        mc.entityRenderer.disableLightmap();
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBegin(GL11.GL_LINE_STRIP);

        double baseMove = (drawPercent > 0.5) ? 1 - drawPercent : drawPercent;
        double min = (height / 60) * 20 * (1 - baseMove) * (drawMode ? -1 : 1);

        for (int i = 0; i <= 20; i++) {
            double moveFace = (height / 60.0) * i * baseMove;
            if (drawMode) {
                moveFace = -moveFace;
            }

            Vec3 firstPoint = points.get(0);
            GL11.glVertex3d(
                    firstPoint.xCoord - mc.getRenderManager().viewerPosX,
                    firstPoint.yCoord - moveFace - min - mc.getRenderManager().viewerPosY,
                    firstPoint.zCoord - mc.getRenderManager().viewerPosZ
            );

            GL11.glColor4f(customColorRed.getValue().floatValue() / 255f, customColorGreen.getValue().floatValue(), customColorBlue.getValue().floatValue(), 0.7f * (i / 20.0f));

            for (Vec3 vec3 : points) {
                GL11.glVertex3d(
                        vec3.xCoord - mc.getRenderManager().viewerPosX,
                        vec3.yCoord - moveFace - min - mc.getRenderManager().viewerPosY,
                        vec3.zCoord - mc.getRenderManager().viewerPosZ
                );
            }

            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    public static double easeInOutQuad(double x) {
        if (x < 0.5) {
            return 2 * x * x;
        } else {
            return 1 - Math.pow(-2 * x + 2, 2) / 2;
        }
    }


}
