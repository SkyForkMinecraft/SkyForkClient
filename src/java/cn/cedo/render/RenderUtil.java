package cn.cedo.render;

import cn.bzdhyp.module.TargetESP;
import cn.langya.font.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.union4dev.base.Access;
import cn.cedo.misc.ColorUtil;
import unknow.Vector3d;
import unknow.Vector4d;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static cn.cedo.misc.MathUtils.interpolate;
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

    public static void resetColor(Color color) {
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
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


    public static void drawCircle(float x, float y, float radius, int color) {
        glColor(color);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glLineWidth(1F);
        glBegin(GL_POLYGON);
        for (int i = 0; i <= 360; i++)
            glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * radius, y + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glColor4f(1F, 1F, 1F, 1F);
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

    public static int width() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
    }

    public static int height() {
        return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    }


    public static void circleNoSmoothRGB(double x, double y, double radius, int color) {
        radius /= 2;
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        color(color);
        glBegin(GL_TRIANGLE_FAN);

        for (double i = 0; i <= 360; i++) {
            double angle = (i * (Math.PI * 2)) / 360;
            glVertex2d(x + (radius * Math.cos(angle)) + radius, y + (radius * Math.sin(angle)) + radius);
        }

        glEnd();
        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
    }

    // Bad rounded rect method but the shader one requires scaling that sucks
    public static void renderRoundedRect(float x, float y, float width, float height, float radius, int color) {
        RenderUtil.drawGoodCircle(x + radius, y + radius, radius, color);
        RenderUtil.drawGoodCircle(x + width - radius, y + radius, radius, color);
        RenderUtil.drawGoodCircle(x + radius, y + height - radius, radius, color);
        RenderUtil.drawGoodCircle(x + width - radius, y + height - radius, radius, color);

        Gui.drawRect2(x + radius, y, width - radius * 2, height, color);
        Gui.drawRect2(x, y + radius, width, height - radius * 2, color);
    }


    // TODO: Replace this with a shader as GL_POINTS is not consistent with gui scales
    public static void drawGoodCircle(double x, double y, float radius, int color) {
        color(color);
        GLUtil.setup2DRendering();

        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        glPointSize(radius * (2 * mc.gameSettings.guiScale));

        glBegin(GL_POINTS);
        glVertex2d(x, y);
        glEnd();

        GLUtil.end2DRendering();
    }


    public static void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor) {
        GLUtil.setup2DRendering();
        glEnable(GL_LINE_SMOOTH);
        glShadeModel(GL_SMOOTH);
        glPushMatrix();
        glBegin(GL_QUADS);
        color(startColor);
        glVertex2d(left, top);
        glVertex2d(left, bottom);
        color(endColor);
        glVertex2d(right, bottom);
        glVertex2d(right, top);
        glEnd();
        glPopMatrix();
        glDisable(GL_LINE_SMOOTH);
        GLUtil.end2DRendering();
        resetColor();
    }

    public static void drawGradientRectBordered(double left, double top, double right, double bottom, double width, int startColor, int endColor, int borderStartColor, int borderEndColor) {
        drawGradientRect(left + width, top + width, right - width, bottom - width, startColor, endColor);
        drawGradientRect(left + width, top, right - width, top + width, borderStartColor, borderEndColor);
        drawGradientRect(left, top, left + width, bottom, borderStartColor, borderEndColor);
        drawGradientRect(right - width, top, right, bottom, borderStartColor, borderEndColor);
        drawGradientRect(left + width, bottom - width, right - width, bottom, borderStartColor, borderEndColor);
    }

    private static float drawExhiOutlined(String text, float x, float y, int borderColor, int mainColor) {
        FontManager.MB14.drawString(text, x, y - (float) 0.35, borderColor);
        FontManager.MB14.drawString(text, x, y + (float) 0.35, borderColor);
        FontManager.MB14.drawString(text, x - (float) 0.35, y, borderColor);
        FontManager.MB14.drawString(text, x + (float) 0.35, y, borderColor);
        FontManager.MB14.drawString(text, x, y, mainColor);
        return x + FontManager.MB14.getStringWidth(text) - 2F;
    }

    public static void drawExhiEnchants(ItemStack stack, float x, float y) {
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();
        disableBlend();
        GlStateManager.resetColor();
        final int darkBorder = 0xFF000000;
        if (stack.getItem() instanceof ItemArmor) {
            int prot = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
            int unb = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            int thorn = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
            if (prot > 0) {
                drawExhiOutlined(prot + "", drawExhiOutlined("P", x, y, darkBorder, -1) + 2, y, getBorderColor(prot), getMainColor(prot));
                y += 5;
            }
            if (unb > 0) {
                drawExhiOutlined(unb + "", drawExhiOutlined("U", x, y, darkBorder, -1) + 2, y, getBorderColor(unb), getMainColor(unb));
                y += 5;
            }
            if (thorn > 0) {
                drawExhiOutlined(thorn + "", drawExhiOutlined("T", x, y, darkBorder, -1) + 2, y, getBorderColor(thorn), getMainColor(thorn));
                y += 5;
            }
        }
        if (stack.getItem() instanceof ItemBow) {
            int power = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            int punch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            int flame = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
            int unb = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (power > 0) {
                drawExhiOutlined(power + "", drawExhiOutlined("Pow", x, y, darkBorder, -1) + 2, y, getBorderColor(power), getMainColor(power));
                y += 5;
            }
            if (punch > 0) {
                drawExhiOutlined(punch + "", drawExhiOutlined("Pun", x, y, darkBorder, -1) + 2, y, getBorderColor(punch), getMainColor(punch));
                y += 5;
            }
            if (flame > 0) {
                drawExhiOutlined(flame + "", drawExhiOutlined("F", x, y, darkBorder, -1) + 2, y, getBorderColor(flame), getMainColor(flame));
                y += 5;
            }
            if (unb > 0) {
                drawExhiOutlined(unb + "", drawExhiOutlined("U", x, y, darkBorder, -1) + 2, y, getBorderColor(unb), getMainColor(unb));
                y += 5;
            }
        }
        if (stack.getItem() instanceof ItemSword) {
            int sharp = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
            int kb = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
            int fire = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            int unb = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (sharp > 0) {
                drawExhiOutlined(sharp + "", drawExhiOutlined("S", x, y, darkBorder, -1) + 2, y, getBorderColor(sharp), getMainColor(sharp));
                y += 5;
            }
            if (kb > 0) {
                drawExhiOutlined(kb + "", drawExhiOutlined("K", x, y, darkBorder, -1) + 2, y, getBorderColor(kb), getMainColor(kb));
                y += 5;
            }
            if (fire > 0) {
                drawExhiOutlined(fire + "", drawExhiOutlined("F", x, y, darkBorder, -1) + 2, y, getBorderColor(fire), getMainColor(fire));
                y += 5;
            }
            if (unb > 0) {
                drawExhiOutlined(unb + "", drawExhiOutlined("U", x, y, darkBorder, -1) + 2, y, getBorderColor(unb), getMainColor(unb));
            }
        }
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
    }

    private static int getMainColor(int level) {
        if (level == 4)
            return 0xFFAA0000;
        return -1;
    }

    private static int getBorderColor(int level) {
        if (level == 2)
            return 0x7055FF55;
        if (level == 3)
            return 0x7000AAAA;
        if (level == 4)
            return 0x70AA0000;
        if (level >= 5)
            return 0x70FFAA00;
        return 0x70FFFFFF;
    }

    public static void circle(final float x, final float y, final float radius, final int fill) {
        arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void circle(final double x, final double y, final double radius, final int fill) {
        arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void arc(final float x, final float y, final float start, final float end, final float radius, final int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arc(final double x, final double y, final double start, final double end, final double radius, final int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h, final int color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var12, var13, var14, var11);
        if (var11 > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float) Math.cos(i * 3.141592653589793 / 180.0) * (w * 1.001f);
                final float ldy = (float) Math.sin(i * 3.141592653589793 / 180.0) * (h * 1.001f);
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (float i = end; i >= start; i -= 4.0f) {
            final float ldx = (float) Math.cos(i * 3.141592653589793 / 180.0) * w;
            final float ldy = (float) Math.sin(i * 3.141592653589793 / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        enableTexture2D();
        disableBlend();
    }

    public static void arcEllipse(final double x, final double y, double start, double end, final double w, final double h, final int color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        double temp;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var12, var13, var14, var11);
        if (var11 > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (double i = end; i >= start; i -= 4.0f) {
                final double ldx = Math.cos(i * 3.141592653589793 / 180.0) * (w * 1.001f);
                final double ldy = Math.sin(i * 3.141592653589793 / 180.0) * (h * 1.001f);
                GL11.glVertex2f((float) (x + ldx), (float) (y + ldy));
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (double i = end; i >= start; i -= 4.0f) {
            final double ldx = Math.cos(i * 3.141592653589793 / 180.0) * w;
            final double ldy = Math.sin(i * 3.141592653589793 / 180.0) * h;
            GL11.glVertex2f((float) (x + ldx), (float) (y + ldy));
        }
        GL11.glEnd();
        enableTexture2D();
        disableBlend();
    }

    public static void quickDrawRect(float x, float y, float x2, float y2) {
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
    }
    public static Color getColor(int color) {
        int f = color >> 24 & 0xFF;
        int f1 = color >> 16 & 0xFF;
        int f2 = color >> 8 & 0xFF;
        int f3 = color & 0xFF;
        return new Color(f1, f2, f3, f);
    }

    public static void startGlScissor(int x, int y, int width, int height) {
        int scaleFactor = new ScaledResolution(mc).getScaleFactor();
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        GL11.glScissor(x * scaleFactor, RenderUtil.mc.displayHeight - (y + height) * scaleFactor, width * scaleFactor, (height += 14) * scaleFactor);
    }

    public static void stopGlScissor() {
        GL11.glDisable(3089);
        GL11.glPopMatrix();
    }

    public static void drawEquippedShit2(final double x, final double y, final EntityLivingBase target) {
        if (!(target instanceof EntityPlayer)) {
            return;
        }
        GL11.glPushMatrix();
        final ArrayList<ItemStack> stuff = new ArrayList<ItemStack>();
        int cock = -2;
        if (target.getHeldItem() != null) {
            stuff.add(target.getHeldItem());
        }
        for (int geraltOfNigeria = 3; geraltOfNigeria >= 0; --geraltOfNigeria) {
            final ItemStack armor = target.getCurrentArmor(geraltOfNigeria);
            if (armor != null) {
                stuff.add(armor);
            }
        }
        for (final ItemStack yes : stuff) {
            if (Minecraft.getMinecraft().theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
                cock += 16;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            GlStateManager.enableBlend();
            GL11.glHint(3155, 4352);
            Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(yes, (int) (cock + x), (int) y);
            Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, yes, (int) (cock + x), (int) y);
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            yes.getEnchantmentTagList();
        }
        GL11.glPopMatrix();
    }


    public static void drawImageRound(ResourceLocation imageLocation, double x, double y, double width, double height, int color, Runnable cutMethod) {
        GlStateManager.pushMatrix();
        StencilUtil.initStencilToWrite();
        cutMethod.run();
        StencilUtil.readStencilBuffer(1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableAlpha();
        mc.getTextureManager().bindTexture(imageLocation);
        RenderUtil.glColor(color);
        Gui.drawModalRectWithCustomSizedTexture((float)x, (float)y, 0.0f, 0.0f, (float)width, (float)height, (float)width, (float)height);
        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        StencilUtil.uninitStencilBuffer();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawTargetESP2D(float x, float y, Color color, Color color2, Color color3, Color color4, float scale, int index, float alpha) {
        long millis = System.currentTimeMillis() + (long) index * 400L;
        double angle = MathHelper.clamp_double((Math.sin((double) millis / 150.0) + 1.0) / 2.0 * 30.0, 0.0, 30.0);
        double scaled = MathHelper.clamp_double((Math.sin((double) millis / 500.0) + 1.0) / 2.0, 0.8, 1.0);
        double rotate = MathHelper.clamp_double((Math.sin((double) millis / 1000.0) + 1.0) / 2.0 * 360.0, 0.0, 360.0);
        rotate = (double) 45 - (angle - 15.0) + rotate;
        float size = 128.0f * scale * (float) scaled;
        float x2 = (x -= size / 2.0f) + size;
        float y2 = (y -= size / 2.0f) + size;
        GlStateManager.pushMatrix();
        RenderUtil.customRotatedObject2D(x, y, size, size, (float) rotate);
        GL11.glDisable(3008);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.shadeModel(7425);
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        drawESPImage(getESPImage(), x, y, x2, y2, color, color2, color3, color4, alpha);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.resetColor();
        GlStateManager.shadeModel(7424);
        GlStateManager.depthMask(true);
        GL11.glEnable(3008);
        GlStateManager.popMatrix();
    }

    private static ResourceLocation getESPImage() {
        switch (TargetESP.mode.getValue()) {
            case "Nurikzapen":
                return new ResourceLocation("client/capture.png");
            case "Round":
                return new ResourceLocation("client/round.png");
        }
        return null;
    }

    private static void drawESPImage(ResourceLocation resource, double x, double y, double x2, double y2, Color c, Color c2, Color c3, Color c4, float alpha) {
        mc.getTextureManager().bindTexture(resource);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
        bufferbuilder.begin(9, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(x, y2, 0.0).tex(0.0, 1.0).color(c.getRed(), c.getGreen(), c.getBlue(), (int) (alpha * 255)).endVertex();
        bufferbuilder.pos(x2, y2, 0.0).tex(1.0, 1.0).color(c2.getRed(), c2.getGreen(), c2.getBlue(), (int) (alpha * 255)).endVertex();
        bufferbuilder.pos(x2, y, 0.0).tex(1.0, 0.0).color(c3.getRed(), c3.getGreen(), c3.getBlue(), (int) (alpha * 255)).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex(0.0, 0.0).color(c4.getRed(), c4.getGreen(), c4.getBlue(), (int) (alpha * 255)).endVertex();
        GlStateManager.shadeModel(7425);
        GlStateManager.depthMask(false);
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.shadeModel(7424);
    }


    public static void customRotatedObject2D(float oXpos, float oYpos, float oWidth, float oHeight, float rotate) {
        GL11.glTranslated((double)(oXpos + oWidth / 2.0F), (double)(oYpos + oHeight / 2.0F), 0.0);
        GL11.glRotated((double)rotate, 0.0, 0.0, 1.0);
        GL11.glTranslated((double)(-oXpos - oWidth / 2.0F), (double)(-oYpos - oHeight / 2.0F), 0.0);
    }

    public static Vector2f targetESPSPos(EntityLivingBase entity) {
        EntityRenderer entityRenderer = mc.entityRenderer;
        float partialTicks = mc.timer.renderPartialTicks;
        int scaleFactor = (new ScaledResolution(mc)).getScaleFactor();
        double x = interpolate(entity.posX, entity.prevPosX, (double)partialTicks);
        double y = interpolate(entity.posY, entity.prevPosY, (double)partialTicks);
        double z = interpolate(entity.posZ, entity.prevPosZ, (double)partialTicks);
        double height = (double)(entity.height / (entity.isChild() ? 1.75F : 1.0F) / 2.0F);
        double width = 0.0;
        AxisAlignedBB aabb = new AxisAlignedBB(x - 0.0, y, z - 0.0, x + 0.0, y + height, z + 0.0);
        Vector3d[] vectors = new Vector3d[]{new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
        entityRenderer.setupCameraTransform(partialTicks, 0);
        Vector4d position = null;
        Vector3d[] vecs3 = vectors;
        int vecLength = vectors.length;

        for(int vecI = 0; vecI < vecLength; ++vecI) {
            Vector3d vector = vecs3[vecI];
            vector = project2D(scaleFactor, vector.getX() - mc.getRenderManager().viewerPosX, vector.getY() - mc.getRenderManager().viewerPosY, vector.getZ() - mc.getRenderManager().viewerPosZ);
            if (vector != null && vector.getZ() >= 0.0 && vector.getZ() < 1.0) {
                if (position == null) {
                    position = new Vector4d(vector.getX(), vector.getY(), vector.getZ(), 0.0);
                }

                position.x = Math.min(vector.getX(), position.x);
                position.y = Math.min(vector.getY(), position.y);
                position.z = Math.max(vector.getX(), position.z);
                position.w = Math.max(vector.getY(), position.w);
            }
        }

        entityRenderer.setupOverlayRendering();
        if (position != null) {
            return new Vector2f((float)position.x, (float)position.y);
        } else {
            return null;
        }
    }
    private static Vector3d project2D(int scaleFactor, double x, double y, double z) {
        IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        return GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, vector) ? new Vector3d((double)(vector.get(0) / (float)scaleFactor), (double)(((float) Display.getHeight() - vector.get(1)) / (float)scaleFactor), (double)vector.get(2)) : null;
    }

}
