package cn.langya.elements.impls;

import cn.cedo.shader.RoundedUtil;
import cn.langya.elements.Element;
import cn.langya.font.FontManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.union4dev.base.Access;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ColorValue;
import org.union4dev.base.value.impl.NumberValue;

import java.awt.*;
import java.util.Collection;


public class PotionInfo extends Element {
    private static final BooleanValue background = new BooleanValue("背景",true);
    private static final NumberValue customRadius = new NumberValue("自定义圆角值", 2,0,10,1);
    private ColorValue color = new ColorValue("背景不透明度", new Color(0, 0, 0, 80));

    public PotionInfo() {
        super(10, 10);
    }

    @Override
    public void draw() {
        if (!Access.getInstance().getModuleManager().isEnabled(getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }

        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x,y,0);
        ScaledResolution sr = new ScaledResolution(mc);
        float i = x * sr.getScaledWidth();
        float j = y * sr.getScaledHeight();
        width = 166;
        Collection<PotionEffect> collection = mc.thePlayer.getActivePotionEffects();

        if (!collection.isEmpty()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            int l = 33;

            if (collection.size() > 5) {
                l = 132 / (collection.size() - 1);
            }

            for (PotionEffect potioneffect : mc.thePlayer.getActivePotionEffects()) {
                if (background.getValue()) {
                    RoundedUtil.drawRound(i + 1, j + 1, i + 164, j + l - 1,customRadius.getValue().intValue(), color.getValue());
                }
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                GlStateManager.enableBlend();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));

                if (potion.hasStatusIcon()) {
                    int i1 = potion.getStatusIconIndex();
                    Gui.drawTexturedModalRect(i + 6, j + 7, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                }

                String s1 = I18n.format(potion.getName());

                if (potioneffect.getAmplifier() == 1) {
                    s1 = s1 + " " + I18n.format("enchantment.level.2");
                } else if (potioneffect.getAmplifier() == 2) {
                    s1 = s1 + " " + I18n.format("enchantment.level.3");
                } else if (potioneffect.getAmplifier() == 3) {
                    s1 = s1 + " " + I18n.format("enchantment.level.4");
                }

                FontManager.M14.drawStringWithShadow(s1, i + 10 + 18, j + 6, 16777215);
                String s = Potion.getDurationString(potioneffect);
                FontManager.M14.drawStringWithShadow(s, i + 10 + 18, j + 6 + 10, 8355711);
                j += l;
            }
            setHeight(height);
            height = j - y * sr.getScaledHeight();
        }
        GlStateManager.popMatrix();
    }
}
