package cn.langya.utils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;
import cn.cedo.render.GLUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author LangYa
 * @since 2024/6/1 上午11:20
 */

public class BufferImageUtil {

    public static BufferedImage mp4toBufferImage(File file) {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file);
        Java2DFrameConverter converter = new Java2DFrameConverter();

        try {
            grabber.start();
            BufferedImage image = converter.convert(grabber.grab());
            grabber.stop();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    private void updateDynamicTexture(BufferedImage image) {
        if (dynamicTexture == null) {
            dynamicTexture = new DynamicTexture(image);
            currentFrameResource = new ResourceLocation("current_frame");
            mc.getTextureManager().loadTexture(currentFrameResource, dynamicTexture);
        } else {
            if (image.getWidth() != dynamicTexture.getGlTextureId() || image.getHeight() != dynamicTexture.getGlTextureId()) {
                dynamicTexture.deleteGlTexture();
                dynamicTexture = new DynamicTexture(image);
                mc.getTextureManager().loadTexture(currentFrameResource, dynamicTexture);
            } else {
                dynamicTexture.updateDynamicTexture();
            }
        }
    }


    private BufferedImage grabFrame() {
        try {
            long currentTime = System.nanoTime();
            if ((currentTime - lastFrameTime) < frameInterval) {
                return null;
            }
            lastFrameTime = currentTime;

            if (grabber == null) {
                System.err.println("FFmpegFrameGrabber is null");
                return null;
            }

            Frame frame = grabber.grabImage();
            if (frame == null) {
                System.err.println("Frame is null");
                return null;
            }

            if (converter == null) {
                System.err.println("Java2DFrameConverter is null");
                return null;
            }

            BufferedImage originalImage = converter.convert(frame);
            if (originalImage == null) {
                System.err.println("Original image is null");
                return null;
            }

            int scaledWidth = this.width * 2;
            int scaledHeight = this.height * 2;
            BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();
            return scaledImage;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    */
    public static int bufferImageToTexture(BufferedImage image){

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

    public static void drawMP4(File file, float x, float y,float width,float height) {
        BufferedImage input = mp4toBufferImage(file);

        GLUtil.startBlend();
        GlStateManager.bindTexture(bufferImageToTexture(input));
        glBindTexture(GL_TEXTURE_2D, bufferImageToTexture(input));
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GLUtil.endBlend();
    }

}
