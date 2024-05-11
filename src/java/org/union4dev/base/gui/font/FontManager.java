package org.union4dev.base.gui.font;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontManager {
    public static CFontRenderer F14 = new CFontRenderer(getFont(new ResourceLocation("client/fonts/misans.ttf"), 14));
    public static CFontRenderer F16 = new CFontRenderer(getFont(new ResourceLocation("client/fonts/misans.ttf"), 16));
    public static CFontRenderer F18 = new CFontRenderer(getFont(new ResourceLocation("client/fonts/misans.ttf"), 18));
    public static CFontRenderer F50 = new CFontRenderer(getFont(new ResourceLocation("client/fonts/misans.ttf"), 50));
    private static Font getFont(ResourceLocation resourceLocation, float fontSize) {
        try {
            Font output = Font.createFont(Font.PLAIN, Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream());
            output = output.deriveFont(fontSize);
            return output;
        } catch (Exception e) {
            return null;
        }
    }
}
