package org.union4dev.base.module.render;

import cn.langya.event.ShaderType;
import cn.superskidder.BloomUtil;
import cn.superskidder.GaussianBlur;
import cn.superskidder.KawaseBlur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Startup;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.events.render.Render2DEvent;
import cn.langya.font.FontManager;
import org.union4dev.base.events.render.ShaderEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;
import skid.cedo.render.StencilUtil;

import java.util.ArrayList;

import static cn.superskidder.GaussianBlur.createFrameBuffer;

@Startup // enable when client startup
public class HUD implements Access.InstanceAccess {

    /**
     * An Example Bool value
     */
    public BooleanValue array = new BooleanValue("功能列表", true);
    public NumberValue spacing = new NumberValue("功能列表间距", 3, 1, 5, 1);
    public ComboValue logoMode = new ComboValue("标识模式", "英文", "中文", "英文");
    public static NumberValue radius = new NumberValue("模糊半径", 10, 1, 50, 1);
    public static NumberValue iterations = new NumberValue("模糊迭代", 4, 0, 15, 1);
    public static NumberValue shadowRadius = new NumberValue("阴影半径", 6, 0, 20, 1);
    public static NumberValue offset = new NumberValue("模糊偏移", 3, 1, 15, 1);
    public static NumberValue shadowOffset = new NumberValue("阴影偏移", 2, 0, 15, 1);
    public static ComboValue blurMode = new ComboValue("模糊方式", "高斯模糊", "高斯模糊", "川濑模糊");

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        String text = logoMode.getValue().equals("中文") ? "天空分支" : "SkyFork";
        FontManager.M50.drawStringWithShadow(text, 4, 4, -1);
        FontManager.M18.drawStringWithShadow(String.format("[帧率: %s]", Minecraft.getDebugFPS()), (float) (event.getScaledResolution().getScaledWidth() / 2.0), 0, -1);

        if (array.getValue()) {
            int width = event.getScaledResolution().getScaledWidth();
            int y1 = 4;
            ArrayList<Class<?>> enabledModules = new ArrayList();
            for (Class<?> m : access.getModuleManager().getModules()) {
                if (access.getModuleManager().isEnabled(m) && access.getModuleManager().isVisible(m)) {
                    enabledModules.add(m);
                }
            }
            enabledModules.sort((o1, o2) -> FontManager.M18.getStringWidth(access.getModuleManager().format(o2)) - FontManager.M18.getStringWidth(access.getModuleManager().format(o1)));
            for (Class<?> module : enabledModules) {
                int moduleWidth = FontManager.M18.getStringWidth(access.getModuleManager().format(module));
                FontManager.M18.drawStringWithShadow(access.getModuleManager().format(module), width - moduleWidth - 4, y1, -1);
                y1 += FontManager.M18.getHeight() + spacing.getValue().intValue();
            }
        }

    }

    private static Framebuffer bloomFramebuffer = new Framebuffer(1, 1, false);


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
