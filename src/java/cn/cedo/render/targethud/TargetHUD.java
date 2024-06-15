package cn.cedo.render.targethud;


import cn.cedo.misc.GradientColorWheel;
import cn.cedo.render.GLUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import org.union4dev.base.Access;

import java.util.HashMap;

public abstract class TargetHUD implements Access.InstanceAccess {
    @Setter
    protected GradientColorWheel colorWheel;

    @Getter
    @Setter
    private float width, height;
    @Getter
    private final String name;

    public TargetHUD(String name) {
        this.name = name;
    }



    protected void renderPlayer2D(float x, float y, float width, float height, AbstractClientPlayer player) {
        GLUtil.startBlend();
        mc.getTextureManager().bindTexture(player.getLocationSkin());
        Gui.drawScaledCustomSizeModalRect(x, y, (float) 8.0, (float) 8.0, 8, 8, width, height, 64.0F, 64.0F);
        GLUtil.endBlend();
    }

    public abstract void render(float x, float y, float alpha, EntityLivingBase target);

    public abstract void renderEffects(float x, float y, float alpha, boolean glow);

    private static final HashMap<Class<? extends TargetHUD>, TargetHUD> targetHuds = new HashMap<>();

    public static TargetHUD get(String name) {
        return targetHuds.values().stream().filter(hud -> hud.getName().equals(name)).findFirst().orElse(null);
    }

    public static <T extends TargetHUD> T get(Class<T> clazz) {
        return (T) targetHuds.get(clazz);
    }

    public static void init() {
        targetHuds.put(TenacityTargetHUD.class, new TenacityTargetHUD());
        targetHuds.put(OldTenacityTargetHUD.class, new OldTenacityTargetHUD());
        targetHuds.put(RiseTargetHUD.class, new RiseTargetHUD());
        targetHuds.put(VapeTargetHUD.class, new VapeTargetHUD());
        targetHuds.put(ExhiTargetHUD.class, new ExhiTargetHUD());
        targetHuds.put(AkrienTargetHUD.class, new AkrienTargetHUD());
        targetHuds.put(AstolfoTargetHUD.class, new AstolfoTargetHUD());
        targetHuds.put(NovolineTargetHUD.class, new NovolineTargetHUD());
    }

}
