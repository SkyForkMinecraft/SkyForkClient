package cn.langya.utils;

/**
 * @author LangYa466
 * @since 2024/4/11 18:44
 */

public class MouseUtil {
    public static boolean isHovering(float x, float y, float width, float height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}
