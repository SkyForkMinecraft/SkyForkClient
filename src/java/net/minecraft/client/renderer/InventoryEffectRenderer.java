package net.minecraft.client.renderer;

import java.awt.*;
import java.util.Collection;

import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.langya.modules.client.ClientSettings;
import lombok.Getter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import com.skyfork.api.cedo.shader.RoundedUtil;

@Getter
public abstract class InventoryEffectRenderer extends GuiContainer
{
    public static InventoryEffectRenderer instance;
    /** True if there is some potion effect to display */
    private boolean hasActivePotionEffects;

    public InventoryEffectRenderer(Container inventorySlotsIn)
    {
        super(inventorySlotsIn);
        instance = this;
    }


    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        super.initGui();
        updateActivePotionEffects();
    }

    protected void updateActivePotionEffects() {
        if (!mc.thePlayer.getActivePotionEffects().isEmpty()) {

            guiLeft = 160 + (width - xSize - 200) / 2;
            hasActivePotionEffects = true;
        } else {
            guiLeft = (width - xSize) / 2;
            hasActivePotionEffects = false;
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (hasActivePotionEffects)
        {
            drawActivePotionEffects();
        }
    }

    /**
     * Display the potion effects list
     */
    public void drawActivePotionEffects()
    {
        int i = guiLeft - 124;
        int j = guiTop;
        int k = 166;
        Collection<PotionEffect> collection = mc.thePlayer.getActivePotionEffects();

        if (!collection.isEmpty())
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            int l = 33;

            if (collection.size() > 5)
            {
                l = 132 / (collection.size() - 1);
            }

            for (PotionEffect potioneffect : mc.thePlayer.getActivePotionEffects())
            {
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(inventoryBackground);
                if (ClientSettings.betterEffects.getValue()) {
                    RoundedUtil.drawRound(i, j,100, 32,2,new Color(0,0,0,120));
                } else {
                    drawTexturedModalRect(i, j, 0, 166, 140, 32);
                }

                if (potion.hasStatusIcon()) {
                    int i1 = potion.getStatusIconIndex();
                    drawTexturedModalRect(i + 6, j + 7, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                }

                String s1 = I18n.format(potion.getName());

                if (potioneffect.getAmplifier() == 1)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.2");
                }
                else if (potioneffect.getAmplifier() == 2)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.3");
                }
                else if (potioneffect.getAmplifier() == 3)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.4");
                }

                FontManager.M16.drawStringWithShadow(s1, (float)(i + 10 + 18), (float)(j + 6), 16777215);
                String s = Potion.getDurationString(potioneffect);
                FontManager.M14.drawStringWithShadow(s, (float)(i + 10 + 18), (float)(j + 6 + 10), 8355711);
                j += l;
            }
        }
    }

    public void drawActivePotionEffects(int dragX,int dragY,boolean bg,int rad)
    {
        int i = dragX;
        int j = dragY;
        Collection<PotionEffect> collection = mc.thePlayer.getActivePotionEffects();

        if (!collection.isEmpty())
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            int l = 33;

            if (collection.size() > 5)
            {
                l = 132 / (collection.size() - 1);
            }

            for (PotionEffect potioneffect : mc.thePlayer.getActivePotionEffects())
            {
                Potion potion = Potion.potionTypes[potioneffect.getPotionID()];


                String s1 = I18n.format(potion.getName());

                if (potioneffect.getAmplifier() == 1)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.2");
                }
                else if (potioneffect.getAmplifier() == 2)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.3");
                }
                else if (potioneffect.getAmplifier() == 3)
                {
                    s1 = s1 + " " + I18n.format("enchantment.level.4");
                }

                String s = Potion.getDurationString(potioneffect);
                if (bg) RoundedUtil.drawRound(i, j - 5,45 + FontManager.M16.getStringWidth(s) + FontManager.M16.getStringWidth(s1), 28,rad,new Color(0,0,0,120));


                if (potion.hasStatusIcon()) {
                    int i1 = potion.getStatusIconIndex();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    mc.getTextureManager().bindTexture(inventoryBackground);
                    drawTexturedModalRect(i + 6, j , i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                FontManager.M16.drawStringWithShadow(s1, (float)(i + 10 + 18), (float)(j), 16777215);
                FontManager.M14.drawStringWithShadow(s, (float)(i + 10 + 18), (float)(j + 10), 8355711);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                j += l;
            }
        }
    }

}
