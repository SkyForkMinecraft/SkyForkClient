package cn.imflowow;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;
import skid.cedo.misc.ColorUtil;

public class BlockOverlay implements Access.InstanceAccess{
    private final ComboValue mode = new ComboValue("模式", "边框", "边框", "填充");
    private final NumberValue lineWidth = new NumberValue("边框大小", 0, 2, 5, 1);
    private static final NumberValue customColorAplha = new NumberValue("填充不透明度", 80, 1, 255, 5);
    private static final ComboValue colorMode = new ComboValue("颜色模式", "客户端", "客户端", "自定义", "彩虹");
    private static final NumberValue customColorRed = new NumberValue("自定义红色", 0, 0, 255, 5);
    private static final NumberValue customColorGreen = new NumberValue("自定义绿色", 0, 0, 255, 5);
    private static final NumberValue customColorBlue = new NumberValue("自定义蓝色", 0, 0, 255, 5);

    @EventTarget
    public void onRender3D(BlockOverlayEvent event) {
        event.setCancelled(true);

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glLineWidth(this.lineWidth.getValue().floatValue());
        Color color = null;

        switch (colorMode.getValue()) {
            case "客户端":
                color = Access.CLIENT_COLOR;
                break;
            case "自定义":
                color = new Color(customColorRed.getValue().intValue(), customColorGreen.getValue().intValue(), customColorBlue.getValue().intValue());
                break;
            case "彩虹":
                color = new Color(ColorUtil.getColor(-(1 + 5 * 1.7f), 0.7f, 1));
        }

        if (mode.getValue().equals("填充")) {

            float red = color.getRed() / 255.0f;
            float green = color.getGreen() / 255.0f;
            float blue = color.getBlue() / 255.0f;
            float alpha = customColorAplha.getValue().floatValue() / 255.0f;

            this.drawFilledBoundingBox(event.getBB(), red, green, blue, alpha);
        }
        if (mode.getValue().equals("边框")) {

            float red = color.getRed() / 255.0f;
            float green = color.getGreen() / 255.0f;
            float blue = color.getBlue() / 255.0f;

            GlStateManager.color(red, green, blue, 1);
            final BlockPos pos = mc.objectMouseOver.getBlockPos();
            final Block block = mc.theWorld.getBlockState(pos).getBlock();
            final double n = pos.getX();
            final double x = n - RenderManager.renderPosX;
            final double n2 = pos.getY();
            final double y = n2 - RenderManager.renderPosY;
            final double n3 = pos.getZ();
            final double z = n3 - RenderManager.renderPosZ;
            final double minX = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0 : block.getBlockBoundsMinX();
            final double minY = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0 : block.getBlockBoundsMinY();
            final double minZ = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0 : block.getBlockBoundsMinZ();
            drawOutlinedBoundingBox(new AxisAlignedBB(x + minX - 0.005, y + minY - 0.005, z + minZ - 0.005, x + block.getBlockBoundsMaxX() + 0.005, y + block.getBlockBoundsMaxY() + 0.005, z + block.getBlockBoundsMaxZ() + 0.005));

        }
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private void drawFilledBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(box.minX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(box.minX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(box.minX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(box.minX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(box.minX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.minX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.minZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.maxY, box.maxZ).color(red, green, blue, alpha).endVertex();
        worldRenderer.pos(box.maxX, box.minY, box.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
    }

    public static void drawOutlinedBoundingBox(final AxisAlignedBB aa) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }

}
