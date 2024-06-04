package cn.superskidder.modules;

import cn.langya.font.FontManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.lwjgl.opengl.GL11;
import org.union4dev.base.Access;
import org.union4dev.base.value.impl.ComboValue;

import java.text.DecimalFormat;

public class TNTTimer implements Access.InstanceAccess {
    private static final ComboValue colorMode = new ComboValue("颜色模式","客户端","客户端","自定义","彩虹");

    public static void doRender(EntityTNTPrimed entity) {
        GL11.glPushMatrix();
        float partialTicks = mc.timer.renderPartialTicks;
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks - RenderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks - RenderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks - RenderManager.renderPosZ;
        float scale = 0.065f;
        GlStateManager.resetColor();
        GlStateManager.translate(((float) x), ((float) y + entity.height + 0.5f - (entity.height / 2.0f)), ((float) z));
        GlStateManager.rotate((-mc.getRenderManager().playerViewY), 0.0f, 1.0f, 0.0f);
        GL11.glScalef((-(scale /= 2.0f)), (-scale), (-scale));
        drawTime(entity);
        GL11.glPopMatrix();
    }

    private static int getWidth(String text) {
        return FontManager.M20.getStringWidth(text);
    }

    private static void drawTime(EntityTNTPrimed entity) {
        float width = (float) getWidth("0.00") / 2.0f + 6.0f;
        DecimalFormat df = new DecimalFormat("0.00");
        FontManager.M20.drawStringWithShadow(df.format((entity.fuse / 20.)), -width + 5.5f, (float) -20.0, -1);
    }

}