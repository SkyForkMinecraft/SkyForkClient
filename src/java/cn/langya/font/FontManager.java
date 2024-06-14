package cn.langya.font;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontManager {
    public static FontDrawer M14 = getFont("MiSans-Bold", 14);
    public static FontDrawer M16 = getFont("MiSans-Bold", 16);
    public static FontDrawer M18 = getFont("MiSans-Bold", 18);
    public static FontDrawer M20 = getFont("MiSans-Bold", 20);
    public static FontDrawer M30 = getFont("MiSans-Bold", 30);
    public static FontDrawer M50 = getFont("MiSans-Bold", 50);

    public static FontDrawer MB14 = getBoldFont("MiSans-Bold", 14);
    public static FontDrawer MB16 = getBoldFont("MiSans-Bold", 16);
    public static FontDrawer MB18 = getBoldFont("MiSans-Bold", 18);
    public static FontDrawer MB30 = getBoldFont("MiSans-Bold", 30);

    public static FontDrawer getFont(String name, int size) {
        Font font;
        try {
            font = Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/" + name + ".ttf")).getInputStream()).deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return new FontDrawer(font, true,true);
    }

    public static FontDrawer getBoldFont(String name, int size) {
        Font font;
        try {
            font = Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/fonts/" + name + ".ttf")).getInputStream()).deriveFont(Font.BOLD, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.BOLD, size);
        }
        return new FontDrawer(font, true,true);
    }

}
