package cn.langya;

import com.jhlabs.image.GaussianFilter;
import lombok.SneakyThrows;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import cn.cedo.render.GLUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import static cn.langya.FontRenderer.loadTexture;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class RoundedRectTest {
    // 毛玻璃过滤器
    private static final GaussianFilter gaussianFilter = new GaussianFilter();

    static {
        gaussianFilter.setRadius(5);
    }

    /**
     * 对 BufferedImage 进行毛玻璃化处理
     *
     * @param bufferedImage
     * @return
     */
    public static BufferedImage doImageBlur(BufferedImage bufferedImage) {
        return gaussianFilter.filter(bufferedImage, null);
    }

    public static void draw(int x, int y, int width, int height, Color color) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();

        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);
        // 圆角
        BufferedImage bufferedImageWithRadius = setRadius(doImageBlur(bufferedImage));
        GLUtil.startBlend();
        GlStateManager.bindTexture(loadTexture(bufferedImageWithRadius));
        glBindTexture(GL_TEXTURE_2D, loadTexture(bufferedImageWithRadius));
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GLUtil.endBlend();
    }

    @SneakyThrows
    public static void drawG2DLogoTest(int x, int y, int width, int height, Color color) {
        File imageFile = new File("C:\\Users\\LangYa\\IdeaProjects\\SkyFork-Client\\src\\resources\\assets\\minecraft\\client\\logo.png");
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setColor(color);
        BufferedImage bufferedImageWithRadius = doImageBlur(bufferedImage);
        GLUtil.startBlend();
        GlStateManager.bindTexture(loadTexture(bufferedImageWithRadius));
        glBindTexture(GL_TEXTURE_2D, loadTexture(bufferedImageWithRadius));
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GLUtil.endBlend();
    }

    /**
     * 图片设置圆角
     *
     * @param srcImage
     * @return
     * @throws
     */
    public static BufferedImage setRadius(BufferedImage srcImage) {
        int radius = (srcImage.getWidth() + srcImage.getHeight()) / 6;
        return setRadius(srcImage, radius, 2, 5);
    }

    /**
     * 图片设置圆角
     *
     * @param srcImage
     * @param radius
     * @param border
     * @param padding
     * @return
     * @throws
     */
    public static BufferedImage setRadius(BufferedImage srcImage, int radius, int border, int padding) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        int canvasWidth = width + padding * 2;
        int canvasHeight = height + padding * 2;

        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();
        gs.setComposite(AlphaComposite.Src);
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setColor(Color.WHITE);
        gs.fill(new RoundRectangle2D.Float(0, 0, canvasWidth, canvasHeight, radius, radius));
        gs.setComposite(AlphaComposite.SrcAtop);
        gs.drawImage(setClip(srcImage, radius), padding, padding, null);
        if (border != 0) {
            gs.setColor(Color.GRAY);
            gs.setStroke(new BasicStroke(border));
            gs.drawRoundRect(padding, padding, canvasWidth - 2 * padding, canvasHeight - 2 * padding, radius, radius);
        }
        gs.dispose();
        return image;
    }

    /**
     * 图片切圆角
     *
     * @param srcImage
     * @param radius
     * @return
     */
    public static BufferedImage setClip(BufferedImage srcImage, int radius) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();

        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setClip(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));
        gs.drawImage(srcImage, 0, 0, null);
        gs.dispose();
        return image;

    }
}
