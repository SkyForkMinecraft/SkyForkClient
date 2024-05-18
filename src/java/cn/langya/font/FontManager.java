package cn.langya.font;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontManager {
    public static FastUniFontRenderer M14 = getFont("MiSans-Bold", 14);
    public static FastUniFontRenderer M16 = getFont("MiSans-Bold", 16);
    public static FastUniFontRenderer M18 = getFont("MiSans-Bold", 18);
    public static FastUniFontRenderer M50 = getFont("MiSans-Bold", 50);
    public static FastUniFontRenderer getFont(String name, int fontSize) {
        try {
            Font output = Font.createFont(Font.PLAIN, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/" + name + ".ttf")).getInputStream());
            output = output.deriveFont(fontSize);
            return new FastUniFontRenderer(output,fontSize,true);
        } catch (Exception e) {
            return null;
        }
    }
    public static Font getJFont(String name, int fontSize) {
        try {
            Font output = Font.createFont(Font.PLAIN, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/" + name + ".ttf")).getInputStream());
            output = output.deriveFont(fontSize);
            return output;
        } catch (Exception e) {
            return null;
        }
    }
}
