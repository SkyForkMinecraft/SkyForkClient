package com.skyfork.client.module.render;

import com.skyfork.api.cedo.misc.ColorUtil;
import com.skyfork.api.cedo.misc.GradientColorWheel;
import com.skyfork.api.cedo.misc.Pair;
import com.skyfork.api.langya.event.ShaderType;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.superskidder.BloomUtil;
import com.skyfork.api.superskidder.GaussianBlur;
import com.skyfork.api.dxg.KawaseBlur;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.annotations.module.Startup;
import com.skyfork.client.events.EventManager;
import com.skyfork.client.events.render.Render2DEvent;
import com.skyfork.client.events.render.ShaderEvent;
import com.skyfork.client.value.impl.ColorValue;
import com.skyfork.client.value.impl.ComboValue;
import com.skyfork.client.value.impl.NumberValue;
import com.skyfork.api.cedo.render.StencilUtil;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static com.skyfork.api.superskidder.GaussianBlur.createFrameBuffer;

@Startup
public class HUD implements Access.InstanceAccess {
    public static final GradientColorWheel colorWheel = new GradientColorWheel();
    public static NumberValue radius = new NumberValue("模糊半径", 10, 1, 50, 1);
    public static NumberValue iterations = new NumberValue("模糊迭代", 4, 1, 15, 1);
    public static NumberValue shadowRadius = new NumberValue("阴影半径", 6, 1, 20, 1);
    public static NumberValue offset = new NumberValue("模糊偏移", 3, 1, 15, 1);
    public static NumberValue shadowOffset = new NumberValue("阴影偏移", 2, 1, 15, 1);
    public static ComboValue blurMode = new ComboValue("模糊方式", "高斯模糊", "高斯模糊", "川濑模糊");
    public static final ColorValue color1 = new ColorValue("Color 1", new Color(0x4A4DAC));
    public static final ColorValue color2 = new ColorValue("Color 2", new Color(0xFFFFFF));
    private static Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);

    private static Framebuffer bloomFramebuffer = new Framebuffer(1, 1, false);
    public static Color color(final int tick) {
        return ColorUtil.fade(5, tick * 20, new Color(Access.CLIENT_COLOR.getRGB()), 1.0f);
    }
    public static Pair<Color, Color> getClientColors() {
        return new Pair<Color, Color>() {
            @Override
            public Color getFirst() {
                return color1.getValue();
            }

            @Override
            public Color getSecond() {
                return color2.getValue();
            }

            @Override
            public <R> R apply(BiFunction<? super Color, ? super Color, ? extends R> func) {
                return null;
            }

            @Override
            public void use(BiConsumer<? super Color, ? super Color> func) {

            }
        };
    }

    @EventTarget
    void onR2d(Render2DEvent e) {
        FontManager.M14.drawString(String.format("XYZ: %s, %s, %s", Math.round(mc.thePlayer.posX) ,Math.round(mc.thePlayer.posY), Math.round(mc.thePlayer.posZ)),2, e.getScaledResolution().getScaledHeight() - FontManager.M14.getHeight() - 3,-1);
        // RenderUtil.drawRect(50,50,50,50,-1);
//        RoundedRectTest.drawG2DLogoTest(50,50,50,50,Color.WHITE);

        /*
        for (Notification notification : Access.getInstance().getNotificationManager().getNotifications()) {
            notification.draw();
            if (!notification.isEnd()) {
                notification.setAnimation(new DecelerateAnimation(300, 1));

            }
            notification.setEnd(notification.getAnimation().finished(Direction.BACKWARDS));
        }

         */
    }

    public static void blurScreen() {
        ScaledResolution sr = new ScaledResolution(mc);

        StencilUtil.initStencilToWrite();
        EventManager.call(new ShaderEvent(ShaderType.Blur));
        StencilUtil.readStencilBuffer(1);
        switch (blurMode.getValue()) {
            case "高斯模糊":
                GaussianBlur.renderBlur(radius.getValue().floatValue());
                break;
            case "川濑模糊":
                stencilFramebuffer = createFrameBuffer(stencilFramebuffer);

                stencilFramebuffer.framebufferClear();
                stencilFramebuffer.bindFramebuffer(false);
                KawaseBlur.renderBlur(stencilFramebuffer.framebufferTexture, iterations.getValue().intValue(), offset.getValue().intValue());
                stencilFramebuffer.unbindFramebuffer();
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
