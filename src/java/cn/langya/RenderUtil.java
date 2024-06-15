package cn.langya;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

/**
 * @author LangYa
 * @since 2024/6/5 下午9:04
 */

public class RenderUtil {

    // skid
    public static int loadTexture(BufferedImage image){

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

        // You now have a ByteBuffer filled with the color data of each pixel.
        // Now just create a texture ID and bind it. Then you can load it using
        // whatever OpenGL method you want, for example:

        int textureID = glGenTextures(); //Generate texture ID
        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

        //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        //Return the texture ID so we can bind it later again
        return textureID;
    }

    public static void drawRect(double left, double top, double right, double bottom, final int color) {
        if (left > right) {
            final double temp = left;
            left = right;
            right = temp;
        }
        if (top > bottom) {
            final double temp = top;
            top = bottom;
            bottom = temp;
        }

        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(r, g, b, a);

        // 绘制带有圆角的矩形
        double radius = Math.min((right - left) / 5, (bottom - top) / 5); // 圆角半径

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        // 左上角
        drawArc(left + radius, top + radius, radius, 180, 270);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        // 右上角
        drawArc(right - radius, top + radius, radius, 270, 360);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        // 右下角
        drawArc(right - radius, bottom - radius, radius, 0, 90);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        // 左下角
        drawArc(left + radius, bottom - radius, radius, 90, 180);
        GL11.glEnd();

        // 中间填充
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(left + radius, top);
        GL11.glVertex2d(right - radius, top);
        GL11.glVertex2d(right - radius, bottom);
        GL11.glVertex2d(left + radius, bottom);

        GL11.glVertex2d(left, top + radius);
        GL11.glVertex2d(left + radius, top + radius);
        GL11.glVertex2d(left + radius, bottom - radius);
        GL11.glVertex2d(left, bottom - radius);

        GL11.glVertex2d(right - radius, top + radius);
        GL11.glVertex2d(right, top + radius);
        GL11.glVertex2d(right, bottom - radius);
        GL11.glVertex2d(right - radius, bottom - radius);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopAttrib();
    }

    private static void drawArc(double cx, double cy, double radius, int startAngle, int endAngle) {
        for (int i = startAngle; i <= endAngle; i++) {
            double angle = Math.toRadians(i);
            double x = cx + (radius * Math.cos(angle));
            double y = cy + (radius * Math.sin(angle));
            GL11.glVertex2d(x, y);
        }
    }

    // Scales the data that you put in the runnable
    public static void scaleStart(float x, float y, float scale) {
        glPushMatrix();
        glTranslatef(x, y, 0);
        glScalef(scale, scale, 1);
        glTranslatef(-x, -y, 0);
    }

    public static void scaleEnd() {
        glPopMatrix();
    }


}
