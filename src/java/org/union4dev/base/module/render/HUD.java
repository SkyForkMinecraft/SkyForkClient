package org.union4dev.base.module.render;

import cn.cedo.render.RenderUtil;
import cn.cedo.shader.GradientUtil;
import cn.cedo.shader.RoundedUtil;
import cn.langya.event.ShaderType;
import cn.langya.font.FontManager;
import cn.superskidder.BloomUtil;
import cn.superskidder.GaussianBlur;
import cn.superskidder.KawaseBlur;
import net.minecraft.client.shader.Framebuffer;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Startup;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.events.render.ShaderEvent;
import org.union4dev.base.value.impl.ColorValue;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;
import cn.cedo.render.StencilUtil;

import java.awt.*;

import static cn.superskidder.GaussianBlur.createFrameBuffer;

@Startup
public class HUD implements Access.InstanceAccess {

    public static NumberValue radius = new NumberValue("模糊半径", 10, 1, 50, 1);
    public static NumberValue iterations = new NumberValue("模糊迭代", 4, 0, 15, 1);
    public static NumberValue shadowRadius = new NumberValue("阴影半径", 6, 0, 20, 1);
    public static NumberValue offset = new NumberValue("模糊偏移", 3, 1, 15, 1);
    public static NumberValue shadowOffset = new NumberValue("阴影偏移", 2, 0, 15, 1);
    public static ComboValue blurMode = new ComboValue("模糊方式", "高斯模糊", "高斯模糊", "川濑模糊");
    public static final ColorValue color1 = new ColorValue("Color 1", new Color(0x4A4DAC));
    public static final ColorValue color2 = new ColorValue("Color 2", new Color(0xFFFFFF));

    private static Framebuffer bloomFramebuffer = new Framebuffer(1, 1, false);

    @EventTarget
    void onR2d(Render2DEvent e) {
//        RoundedRectTest.drawG2DLogoTest(50,50,50,50,Color.WHITE);
    }

    public static void blurScreen() {

        StencilUtil.initStencilToWrite();
        EventManager.call(new ShaderEvent(ShaderType.Blur));
        StencilUtil.readStencilBuffer(1);
        switch (blurMode.getValue()) {
            case "高斯模糊":
                GaussianBlur.renderBlur(radius.getValue().floatValue());
                break;
            case "川濑模糊":
                KawaseBlur.renderBlur(iterations.getValue().intValue(), offset.getValue().intValue());
                break;
        }
        StencilUtil.uninitStencilBuffer();


        bloomFramebuffer = createFrameBuffer(bloomFramebuffer);
        bloomFramebuffer.framebufferClear();
        bloomFramebuffer.bindFramebuffer(true);
        EventManager.call(new ShaderEvent(ShaderType.Shadow));
        bloomFramebuffer.unbindFramebuffer();
        BloomUtil.renderBlur(bloomFramebuffer.framebufferTexture, shadowRadius.getValue().intValue(), shadowOffset.getValue().intValue());
    }
}
