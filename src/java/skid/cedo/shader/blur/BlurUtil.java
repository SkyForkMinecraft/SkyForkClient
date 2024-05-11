package skid.cedo.shader.blur;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.union4dev.base.Access;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.events.render.ShaderEvent;
import skid.cedo.render.RenderUtil;
import skid.cedo.render.StencilUtil;
import skid.cedo.shader.ShaderUtil;

/**
 * @author cedo
 * @since 05/18/2022
 */
public class BlurUtil implements Access.InstanceAccess {

  private static final ShaderUtil gaussianBlur = new ShaderUtil("shaders/gaussian-blur.frag");

  private static Framebuffer blurBuffer = new Framebuffer(1, 1, false);


  public static void renderGaussianBlur(float radius, float compression) {
    ScaledResolution sr = new ScaledResolution(mc);

    GlStateManager.enableBlend();
    OpenGlHelper.glBlendFunc(
            GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

    StencilUtil.initStencilToWrite();
    EventManager.call(new ShaderEvent(false));

    StencilUtil.readStencilBuffer(1);

    blurBuffer = RenderUtil.createFrameBuffer(blurBuffer);

    gaussianBlur.init();
    blurUniforms(radius, compression, 0f);
    blurBuffer.framebufferClear();
    blurBuffer.bindFramebuffer(false);
    RenderUtil.bindTexture(mc.getFramebuffer().framebufferTexture);
    ShaderUtil.drawQuads();
    gaussianBlur.unload();
    blurBuffer.unbindFramebuffer();

    gaussianBlur.init();
    blurUniforms(radius, 0f, compression);
    mc.getFramebuffer().bindFramebuffer(false);
    RenderUtil.bindTexture(blurBuffer.framebufferTexture);
    ShaderUtil.drawQuads();
    gaussianBlur.unload();

    StencilUtil.uninitStencilBuffer();
  }

  private static void blurUniforms(float radius, float directionX, float directionY) {
    gaussianBlur.setUniformi("u_texture", 0);
    gaussianBlur.setUniformf("texelSize", 1.0f / mc.displayWidth, 1.0f / mc.displayHeight);
    gaussianBlur.setUniformf("radius", radius);
    gaussianBlur.setUniformf("direction", directionX, directionY);
  }
}