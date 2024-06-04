package cn.langya.modules.client;

import cn.langya.font.FontDrawer;
import cn.langya.font.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render3DEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;
import cn.cedo.misc.ColorUtil;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

import static org.union4dev.base.Access.InstanceAccess.mc;

public class ClientSettings {
    public static final BooleanValue rect = new BooleanValue("聊天框背景",false);
    public static final BooleanValue font = new BooleanValue("聊天框文字优化",true);
    private static final ComboValue fontScale = new ComboValue("聊天框文字大小","14","14","16");
    public static BooleanValue betterEffects = new BooleanValue("更好药水显示",true);
    public static BooleanValue betterNameTags = new BooleanValue("更好名称显示",true);
    public static FontDrawer getCustomFont() {
        switch (fontScale.getValue()) {
            case "14": return FontManager.M14;
            case "16": return FontManager.M16;
        }
        return FontManager.M14;
    }

    @EventTarget
    private void renderHealth(Render3DEvent e) {

        final Random random = new Random();
        int width = 0;
        final DecimalFormat decimalFormat = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ENGLISH));

        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        final GuiScreen screen = mc.currentScreen;
        final float absorptionHealth = mc.thePlayer.getAbsorptionAmount();
        final String string = decimalFormat.format(mc.thePlayer.getHealth() / 2.0f) + "§c❤ " + ((absorptionHealth <= 0.0f) ? "" : ("§e" + decimalFormat.format(absorptionHealth / 2.0f) + "§6❤"));
        int offsetY = 0;
        if ((mc.thePlayer.getHealth() >= 0.0f && mc.thePlayer.getHealth() < 10.0f) || (mc.thePlayer.getHealth() >= 10.0f && mc.thePlayer.getHealth() < 100.0f)) {
            width = 3;
        }
        if (screen instanceof GuiInventory) {
            offsetY = 70;
        }
        else if (screen instanceof GuiContainerCreative) {
            offsetY = 80;
        }
        else if (screen instanceof GuiChest) {
            offsetY = ((GuiChest)screen).ySize / 2 - 15;
        }
        final int x = new ScaledResolution(mc).getScaledWidth() / 2 - width;
        final int y = new ScaledResolution(mc).getScaledHeight() / 2 + 25 + offsetY;
        final Color color = ColorUtil.blendColors(new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { new Color(255, 37, 0), Color.YELLOW, Color.GREEN }, mc.thePlayer.getHealth() / mc.thePlayer.getMaxHealth());
        mc.fontRendererObj.drawString(string, (absorptionHealth > 0.0f) ? (x - 15.5f) : (x - 3.5f), (float)y, color.getRGB(), true);
        GL11.glPushMatrix();
        mc.getTextureManager().bindTexture(Gui.icons);
        random.setSeed(mc.ingameGUI.getUpdateCounter() * 312871L);
        final float width1 = scaledResolution.getScaledWidth() / 2.0f - mc.thePlayer.getMaxHealth() / 2.5f * 10.0f / 2.0f;
        final float maxHealth = mc.thePlayer.getMaxHealth();
        final int lastPlayerHealth = mc.ingameGUI.lastPlayerHealth;
        final int healthInt = MathHelper.ceiling_float_int(mc.thePlayer.getHealth());
        int l2 = -1;
        final boolean flag = mc.ingameGUI.healthUpdateCounter > mc.ingameGUI.getUpdateCounter() && (mc.ingameGUI.healthUpdateCounter - mc.ingameGUI.getUpdateCounter()) / 3L % 2L == 1L;
        if (mc.thePlayer.isPotionActive(Potion.regeneration)) {
            l2 = mc.ingameGUI.getUpdateCounter() % MathHelper.ceiling_float_int(maxHealth + 5.0f);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        for (int i6 = MathHelper.ceiling_float_int(maxHealth / 2.0f) - 1; i6 >= 0; --i6) {
            int xOffset = 16;
            if (mc.thePlayer.isPotionActive(Potion.poison)) {
                xOffset += 36;
            }
            else if (mc.thePlayer.isPotionActive(Potion.wither)) {
                xOffset += 72;
            }
            int k3 = 0;
            if (flag) {
                k3 = 1;
            }
            final float renX = width1 + i6 % 10 * 8;
            float renY = scaledResolution.getScaledHeight() / 2.0f + 15.0f + offsetY;
            if (healthInt <= 4) {
                renY += random.nextInt(2);
            }
            if (i6 == l2) {
                renY -= 2.0f;
            }
            int yOffset = 0;
            if (Minecraft.getMinecraft().theWorld.getWorldInfo().isHardcoreModeEnabled()) {
                yOffset = 5;
            }
            Gui.drawTexturedModalRect(renX, renY, 16 + k3 * 9, 9 * yOffset, 9, 9);
            if (flag) {
                if (i6 * 2 + 1 < lastPlayerHealth) {
                    Gui.drawTexturedModalRect(renX, renY, xOffset + 54, 9 * yOffset, 9, 9);
                }
                if (i6 * 2 + 1 == lastPlayerHealth) {
                    Gui.drawTexturedModalRect(renX, renY, xOffset + 63, 9 * yOffset, 9, 9);
                }
            }
            if (i6 * 2 + 1 < healthInt) {
                Gui.drawTexturedModalRect(renX, renY, xOffset + 36, 9 * yOffset, 9, 9);
            }
            if (i6 * 2 + 1 == healthInt) {
                Gui.drawTexturedModalRect(renX, renY, xOffset + 45, 9 * yOffset, 9, 9);
            }
        }
        GL11.glPopMatrix();
    }

}
