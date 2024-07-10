package cn.langya.modules.render;

import anti_leak.Native;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Disable;
import org.union4dev.base.annotations.module.Enable;
import org.union4dev.base.events.update.TickEvent;
import org.union4dev.base.value.impl.NumberValue;
import unknow.MotionBlurResourceManager;

import java.lang.reflect.Field;
import java.util.Map;

@Native
public class MotionBlur implements Access.InstanceAccess {
    public static final NumberValue amount = new NumberValue("模糊度", 2, 1, 10,1);

    float lastValue = 0F;
    private Map<String, MotionBlurResourceManager> domainResourceManagers;

    @Disable
    public void onDisable() {
        mc.entityRenderer.stopUseShader();
    }

    @Enable
    public void onEnable() {
        if(this.domainResourceManagers == null) {
            try {
                Field[] fields = SimpleReloadableResourceManager.class.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        this.domainResourceManagers = (Map<String, MotionBlurResourceManager>) field.get(mc.getResourceManager());
                        break;
                    }
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        if(!this.domainResourceManagers.containsKey("motionblur")) {
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }

        this.lastValue = amount.getValue().floatValue();
        applyShader();
    }

    public boolean isFastRenderEnabled() {
        try {
            Field fastRender = GameSettings.class.getDeclaredField("ofFastRender");
            return fastRender.getBoolean(mc.gameSettings);
        } catch (Exception exception) {
            return false;
        }
    }

    public void applyShader() {
        mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if((!mc.entityRenderer.isShaderActive() || this.lastValue != amount.getValue()) && mc.theWorld != null && !isFastRenderEnabled()) {
            this.lastValue = amount.getValue().floatValue();
            applyShader();
        }
        if(this.domainResourceManagers == null) {
            try {
                Field[] fields = SimpleReloadableResourceManager.class.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        this.domainResourceManagers = (Map<String, MotionBlurResourceManager>) field.get(mc.getResourceManager());
                        break;
                    }
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        if(!this.domainResourceManagers.containsKey("motionblur")) {
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }
    }

}
