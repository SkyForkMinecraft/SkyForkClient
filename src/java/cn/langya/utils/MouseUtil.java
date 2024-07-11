package cn.langya.utils;

import org.lwjgl.input.Mouse;

/**
 * @author LangYa466
 * @since 2024/4/11 18:44
 */

public class MouseUtil {
    public static boolean isHovering(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public static boolean isInside(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX > x && mouseX < (x + width)) && (mouseY > y && mouseY < (y + height));
    }

    public enum Scroll {
        UP, DOWN;
    }

    public static Scroll scroll() {
        int mouse = Mouse.getDWheel();

        if(mouse > 0) {
            return Scroll.UP;
        }else if(mouse < 0) {
            return Scroll.DOWN;
        }else {
            return null;
        }
    }
}
