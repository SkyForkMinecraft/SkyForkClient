package unknow;

import cn.cedo.render.GLUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

public class JavaCVUtil {

    // 主函数
    public static void drawMP4() {
        try {
            String videoPath = "D:\\Bianca-Punishing-Gray-Raven-4K.mp4";  // 原视频文件路径
            int second = 0; // 每隔多少帧取一张图，一般高清视频每秒 20-24 帧，根据情况配置，如果全部提取，则将second设为 0 即可
            // 开始视频取帧流程
            fetchPic(new File(videoPath), second);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定视频的帧并保存为图片至指定目录
     *
     * @param file 源视频文件
     * @throws Exception
     */
    public static void fetchPic(File file, int second) throws Exception {

        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file); // 获取视频文件

        System.out.println(getVideoTime(file)); // 显示视频长度（秒/s）

        ff.start(); // 调用视频文件播放
        int length = ff.getLengthInAudioFrames(); //视频帧数长度
        System.out.println(ff.getFrameRate());

        int i = 0; // 图片帧数，如需跳过前几秒，则在下方过滤即可
        Frame frame;
        int count = 0;
        while (i < length) {
            frame = ff.grabImage(); // 获取该帧图片流
            System.out.print(i + ",");
            if (frame != null && frame.image != null) {
                System.out.println(i);
                draw(frame, count, second);
                // writeToFile(frame, picPath, count,second); // 生成帧图片
                count++;
            }
            i++;
        }
        ff.stop();
    }


    public static int loadTexture(BufferedImage image) {

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
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

    /**
     * @param frame  // 视频文件对象
     * @param count  // 当前取到第几帧
     * @param second // 每隔多少帧取一张，一般高清视频每秒 20-24 帧，根据情况配置，如果全部提取，则将second设为 0 即可
     */
    public static void draw(Frame frame, int count, int second) {
        if (second == 0) {
            // 跳过间隔取帧判断
        } else if (count % second != 0) { // 提取倍数，如每秒取一张，则： second = 20
            return;
        }

        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage input = converter.getBufferedImage(frame);
        int width = input.getWidth();
        int height = input.getHeight();

        GLUtil.startBlend();
        GlStateManager.bindTexture(loadTexture(input));
        glBindTexture(GL_TEXTURE_2D, loadTexture(input));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width, height, width, height);
        GLUtil.endBlend();
    }

    /**
     * 获取视频时长，单位为秒
     *
     * @param file
     * @return 时长（s）
     */
    public static Long getVideoTime(File file) {
        Long times = 0L;
        try {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file);
            ff.start();
            times = ff.getLengthInTime() / (1000 * 1000);
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }
}
