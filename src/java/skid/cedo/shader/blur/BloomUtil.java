package skid.cedo.shader.blur;


import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.union4dev.base.Access;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.events.render.ShaderEvent;
import skid.cedo.render.RenderUtil;
import skid.cedo.shader.ShaderUtil;

/**
 * @author cedo
 * @since 05/18/2022
 */
public class BloomUtil implements Access.InstanceAccess {


    private static final ShaderUtil gaussianBloom = new ShaderUtil("shaders/gaussian-bloom.frag");

    private static Framebuffer contentBuffer = new Framebuffer(1,1, false);
    private static Framebuffer blurBuffer = new Framebuffer(1,1, false);


    public static void renderGaussianBloom(float radius) {

        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        RenderUtil.setAlphaLimit(0);
        RenderUtil.resetColor();

        blurBuffer = RenderUtil.createFrameBuffer(blurBuffer);
        contentBuffer = RenderUtil.createFrameBuffer(contentBuffer);

        contentBuffer.framebufferClear();
        contentBuffer.bindFramebuffer(false);
        EventManager.call(new ShaderEvent(true));

        contentBuffer.unbindFramebuffer();

        GlStateManager.enableBlend();

        gaussianBloom.init();
        blurUniforms(radius, 1, 0f);
        blurBuffer.framebufferClear();
        blurBuffer.bindFramebuffer(false);
        RenderUtil.bindTexture(contentBuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();
        blurBuffer.unbindFramebuffer();


        mc.getFramebuffer().bindFramebuffer(true);

        gaussianBloom.init();
        blurUniforms(radius, 0f, 1);
        GL13.glActiveTexture(GL13.GL_TEXTURE20);
        RenderUtil.bindTexture(contentBuffer.framebufferTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        RenderUtil.bindTexture(blurBuffer.framebufferTexture);

        ShaderUtil.drawQuads();
        gaussianBloom.unload();

        RenderUtil.resetColor();
        GlStateManager.bindTexture(0);
        GlStateManager.alphaFunc(516, 0.1f);

    }


    private static void blurUniforms(float radius, float directionX, float directionY) {
        gaussianBloom.setUniformi("u_texture", 0);
        gaussianBloom.setUniformi("u_texture2", 20);
        gaussianBloom.setUniformf("texelSize", 1.0f / mc.displayWidth, 1.0f / mc.displayHeight);
        gaussianBloom.setUniformf("radius", radius);
        gaussianBloom.setUniformf("direction", directionX, directionY);
    }

}