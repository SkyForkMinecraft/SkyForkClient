package com.skyfork.api.alan.impl;

import com.skyfork.api.alan.base.RiseShader;
import com.skyfork.api.alan.base.RiseShaderProgram;
import com.skyfork.api.alan.base.ShaderRenderType;
import com.skyfork.api.alan.base.ShaderUniforms;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

public class MainMenuBackgroundShader extends RiseShader {

    private static RiseShaderProgram program = new RiseShaderProgram("login.fsh", "vertex.vsh");;

    private static Framebuffer tempFBO = new Framebuffer(mc.displayWidth, mc.displayHeight, true);

    private static List<String> bgNames = Arrays.asList("mainmenu", "login", "load", "mtf","background","background2");
    static int bg = 0;

    public static void doSwitchBG() {
        if (bg >= bgNames.size()) {
            bg = 0;
        }
        String name = bgNames.get(bg);
        System.out.println(bg + "-" + name);
        program = new RiseShaderProgram(name + ".fsh", "vertex.vsh");

        ++bg;
    }

    @Override
    public void run(ShaderRenderType type, float partialTicks, List<Runnable> runnable) {
        // Prevent rendering
        if (!Display.isVisible()) {
            return;
        }

        if (type == ShaderRenderType.OVERLAY) {
            this.update();

            // program ids
            final int programID = this.program.getProgramId();

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableAlpha();

            mc.getFramebuffer().bindFramebuffer(true);
            this.program.start();
            ShaderUniforms.uniform2f(programID, "resolution", mc.displayWidth, mc.displayHeight);
            ShaderUniforms.uniform1f(programID, "time", (System.currentTimeMillis() - mc.getStartMillisTime()) / 1000F);
            RiseShaderProgram.drawQuad();
            RiseShaderProgram.stop();
        }
    }

    @Override
    public void update() {
        // can be true since this is only called in gui screen
        this.setActive(true);

        if (mc.displayWidth != tempFBO.framebufferWidth || mc.displayHeight != tempFBO.framebufferHeight) {
            tempFBO.deleteFramebuffer();
            tempFBO = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        } else {
            tempFBO.framebufferClear();
        }
    }
}