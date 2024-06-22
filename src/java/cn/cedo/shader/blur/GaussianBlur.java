package cn.cedo.shader.blur;

import cn.cedo.shader.RoundedUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.union4dev.base.Access;
import cn.cedo.misc.MathUtils;
import cn.cedo.render.RenderUtil;
import cn.cedo.render.StencilUtil;
import cn.cedo.shader.ShaderUtil;

import java.awt.*;
import java.nio.FloatBuffer;

/**
 * @author cedo
 * @since 05/13/2022
 */
public class GaussianBlur implements Access.InstanceAccess {

    private static final ShaderUtil gaussianBlur = new ShaderUtil("client/shaders/gaussian.frag");

    private static Framebuffer framebuffer = new Framebuffer(1, 1, false);

    private static void setupUniforms(float dir1, float dir2, float radius) {
        gaussianBlur.setUniformi("textureIn", 0);
        gaussianBlur.setUniformf("texelSize", 1.0F / mc.displayWidth, 1.0F / mc.displayHeight);
        gaussianBlur.setUniformf("direction", dir1, dir2);
        gaussianBlur.setUniformf("radius", radius);

        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(MathUtils.calculateGaussianValue(i, radius / 2));
        }

        weightBuffer.rewind();
        GL20.glUniform1(gaussianBlur.getUniform("weights"), weightBuffer);
    }

    public static void render(float x,float y,float width,float height,float rectRadius,int blurRadius) {
        startBlur();
        RoundedUtil.drawRound(x,y,width,height,rectRadius, Color.white);
        endBlur(blurRadius,2);
    }
    public static void startBlur() {
        StencilUtil.initStencilToWrite();
    }

    public static void endBlur(float radius, float compression) {
        StencilUtil.readStencilBuffer(1);

        framebuffer = RenderUtil.createFrameBuffer(framebuffer);

        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(false);
        gaussianBlur.init();
        setupUniforms(compression, 0, radius);

        RenderUtil.bindTexture(mc.getFramebuffer().framebufferTexture);
        ShaderUtil.drawQuads();
        framebuffer.unbindFramebuffer();
        gaussianBlur.unload();

        mc.getFramebuffer().bindFramebuffer(false);
        gaussianBlur.init();
        setupUniforms(0, compression, radius);

        RenderUtil.bindTexture(framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        gaussianBlur.unload();

        StencilUtil.uninitStencilBuffer();
        RenderUtil.resetColor();
        GlStateManager.bindTexture(0);

    }

}
