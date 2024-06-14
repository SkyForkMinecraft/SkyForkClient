package cn.cedo.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.union4dev.base.Access;
import cn.cedo.misc.ColorUtil;

import java.awt.*;

import static net.minecraft.client.renderer.GlStateManager.disableBlend;
import static net.minecraft.client.renderer.GlStateManager.enableTexture2D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

/**
 * @author LangYa466
 * @skiddata 2024/4/25 19:57
 */

public class RenderUtil implements Access.InstanceAccess {


    public static void drawBorder(double x, double y, double width, double height, float borderWidth, int color) {
        resetColor();
        setAlphaLimit(0);
        GLUtil.setup2DRendering(true);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        // Draw top border
        worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(x, y, 0.0D).color(color).endVertex();
        worldrenderer.pos(x, y + borderWidth, 0.0D).color(color).endVertex();
        worldrenderer.pos(x + width, y + borderWidth, 0.0D).color(color).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).color(color).endVertex();
        tessellator.draw();

        // Draw bottom border
        worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(x, y + height - borderWidth, 0.0D).color(color).endVertex();
        worldrenderer.pos(x, y + height, 0.0D).color(color).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).color(color).endVertex();
        worldrenderer.pos(x + width, y + height - borderWidth, 0.0D).color(color).endVertex();
        tessellator.draw();

        // Draw left border
        worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(x, y + borderWidth, 0.0D).color(color).endVertex();
        worldrenderer.pos(x, y + height - borderWidth, 0.0D).color(color).endVertex();
        worldrenderer.pos(x + borderWidth, y + height - borderWidth, 0.0D).color(color).endVertex();
        worldrenderer.pos(x + borderWidth, y + borderWidth, 0.0D).color(color).endVertex();
        tessellator.draw();

        // Draw right border
        worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(x + width - borderWidth, y + borderWidth, 0.0D).color(color).endVertex();
        worldrenderer.pos(x + width - borderWidth, y + height - borderWidth, 0.0D).color(color).endVertex();
        worldrenderer.pos(x + width, y + height - borderWidth, 0.0D).color(color).endVertex();
        worldrenderer.pos(x + width, y + borderWidth, 0.0D).color(color).endVertex();
        tessellator.draw();

        GLUtil.end2DRendering();
    }

    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }

    public static void resetColor() {
        GlStateManager.color(1, 1, 1, 1);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        return createFrameBuffer(framebuffer, false);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer, boolean depth) {
        if (needsNewFramebuffer(framebuffer)) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(mc.displayWidth, mc.displayHeight, depth);
        }
        return framebuffer;
    }

    public static boolean needsNewFramebuffer(Framebuffer framebuffer) {
        return framebuffer == null || framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight;
    }

    public static void bindTexture(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public static void drawRect(float left, float top, float right, float bottom, final int color) {
        if (left < right) {
            final float e = left;
            left = right;
            right = e;
        }
        if (top < bottom) {
            final float e = top;
            top = bottom;
            bottom = e;
        }
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float b = (color >> 16 & 0xFF) / 255.0f;
        final float c = (color >> 8 & 0xFF) / 255.0f;
        final float d = (color & 0xFF) / 255.0f;
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(b, c, d, a);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0).endVertex();
        worldRenderer.pos(right, bottom, 0.0).endVertex();
        worldRenderer.pos(right, top, 0.0).endVertex();
        worldRenderer.pos(left, top, 0.0).endVertex();
        Tessellator.getInstance().draw();
        enableTexture2D();
        disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    public static void drawArc(float x1, float y1, double r, int color, int startPoint, double arc, int linewidth) {
        r *= 2.0;
        x1 *= 2.0F;
        y1 *= 2.0F;
        float f = (color >> 24 & 255) / 255.0F;
        float f1 = (color >> 16 & 255) / 255.0F;
        float f2 = (color >> 8 & 255) / 255.0F;
        float f3 = (color & 255) / 255.0F;
        glDisable(2929);
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glDepthMask(true);
        glEnable(2848);
        glHint(3154, 4354);
        glHint(3155, 4354);
        glScalef(0.5F, 0.5F, 0.5F);
        glLineWidth(linewidth);
        glEnable(2848);
        glColor4f(f1, f2, f3, f);
        glBegin(3);

        for (int i = startPoint; i <= arc; ++i) {
            double x = Math.sin(i * Math.PI / 180.0) * r;
            double y = Math.cos(i * Math.PI / 180.0) * r;
            glVertex2d(x1 + x, y1 + y);
        }

        glEnd();
        glDisable(2848);
        glScalef(2.0F, 2.0F, 2.0F);
        glEnable(3553);
        glDisable(3042);
        glEnable(2929);
        glDisable(2848);
        glHint(3154, 4352);
        glHint(3155, 4352);
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void doGlScissor(final float x, final float y, final float windowWidth2, final float windowHeight2) {
        int scaleFactor = 1;
        float k = mc.gameSettings.guiScale;
        if (k == 0.0f) {
            k = 1000.0f;
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        glScissor((int) (x * scaleFactor), (int) (mc.displayHeight - (y + windowHeight2) * scaleFactor), (int) (windowWidth2 * scaleFactor), (int) (windowHeight2 * scaleFactor));
    }

    public static void drawArc(float x1, float y1, double r, int color, int startPoint, double arc, int linewidth, boolean rainbow) {
        r *= 2.0;
        x1 *= 2.0F;
        y1 *= 2.0F;
        float f = (color >> 24 & 255) / 255.0F;
        float f1 = (color >> 16 & 255) / 255.0F;
        float f2 = (color >> 8 & 255) / 255.0F;
        float f3 = (color & 255) / 255.0F;
        glDisable(2929);
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glDepthMask(true);
        glEnable(2848);
        glHint(3154, 4354);
        glHint(3155, 4354);
        glScalef(0.5F, 0.5F, 0.5F);
        glLineWidth(linewidth);
        glEnable(2848);
        glColor4f(f1, f2, f3, f);
        glBegin(3);

        for (int i = startPoint; i <= arc; ++i) {
            if (rainbow) glColor(ColorUtil.rainbow(-(long) (1.0E10f / 360 * i)).getRGB());
            double x = Math.sin(i * Math.PI / 180.0) * r;
            double y = Math.cos(i * Math.PI / 180.0) * r;
            glVertex2d(x1 + x, y1 + y);
        }

        glEnd();
        glDisable(2848);
        glScalef(2.0F, 2.0F, 2.0F);
        glEnable(3553);
        glDisable(3042);
        glEnable(2929);
        glDisable(2848);
        glHint(3154, 4352);
        glHint(3155, 4352);
    }


    public static void drawRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double e = left;
            left = right;
            right = e;
        }
        if (top < bottom) {
            final double e = top;
            top = bottom;
            bottom = e;
        }
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float b = (color >> 16 & 0xFF) / 255.0f;
        final float c = (color >> 8 & 0xFF) / 255.0f;
        final float d = (color & 0xFF) / 255.0f;
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(b, c, d, a);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0.0).endVertex();
        worldRenderer.pos(right, bottom, 0.0).endVertex();
        worldRenderer.pos(right, top, 0.0).endVertex();
        worldRenderer.pos(left, top, 0.0).endVertex();
        Tessellator.getInstance().draw();
        enableTexture2D();
        disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawImage(ResourceLocation resourceLocation, float x, float y, float imgWidth, float imgHeight) {
        GLUtil.startBlend();
        mc.getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0, 0, (int) imgWidth, (int) imgHeight, imgWidth, imgHeight);
        GLUtil.endBlend();
    }

    public static void drawImage(final ResourceLocation image, final int x, final int y, final float width, final float height, final float alpha) {
        glPushMatrix();
        glDisable(2929);
        glEnable(3042);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        glColor4f(1.0f, 1.0f, 1.0f, alpha);
        mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture((float) x, y, 0.0f, 0.0f, (int) width, (int) height, width, height);
        glDepthMask(true);
        glDisable(3042);
        glEnable(2929);
        glPopMatrix();
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawImage(final ResourceLocation image, final float x, final float y, final float width, final float height, Color c) {
        glDisable(2929);
        glEnable(3042);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, (int) width, (int) height, width, height);
        glDepthMask(true);
        glDisable(3042);
        glEnable(2929);
    }


    public static void color(int color, float alpha) {
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, alpha);
    }

    public static void color(int color) {
        color(color, (color >> 24 & 255) / 255.0F);
    }


    public static void drawTexture(int texture, float x, float y, float width, float height, float u, float v, int textureWidth, int textureHeight) {
        float xTexel = 1.0F / textureWidth;
        float yTexel = 1.0F / textureHeight;

        GlStateManager.bindTexture(texture);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0).tex(u * xTexel, (v + height) * yTexel).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0).tex((u + width) * xTexel, (v + height) * yTexel).endVertex();
        worldrenderer.pos(x + width, y, 0.0).tex((u + width) * xTexel, v * yTexel).endVertex();
        worldrenderer.pos(x, y, 0.0).tex(u * xTexel, v * yTexel).endVertex();
        tessellator.draw();
    }

    // liquidbounce
    public static void drawLoadingCircle(float x, float y) {
        for (int i = 0; i < 4; i++) {
            long time = System.nanoTime();
            int rot = (int) ((time / 5000000 * i) % 360);
            drawCircle(x, y, i * 10, rot - 180, rot);
        }
    }

    public static void drawCircle(float x, float y, float radius, int start, int end) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        color(Color.WHITE.getRGB());

        glEnable(GL_LINE_SMOOTH);
        glLineWidth(2F);
        glBegin(GL_LINE_STRIP);
        for (float i = end; i >= start; i -= (360 / 90.0f)) {
            glVertex2f((float) (x + (Math.cos(i * Math.PI / 180) * (radius * 1.001F))), (float) (y + (Math.sin(i * Math.PI / 180) * (radius * 1.001F))));
        }
        glEnd();
        glDisable(GL_LINE_SMOOTH);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }


    // fpsmaster
    public static void drawBoundingBox(final AxisAlignedBB aa) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawOutlinedBoundingBox(final AxisAlignedBB aa) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }
}
