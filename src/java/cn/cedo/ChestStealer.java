package cn.cedo;

import cn.cedo.misc.TimerUtil;
import cn.imflowow.LoadWorldEvent;
import cn.langya.font.FontManager;
import cn.langya.verify.User;
import cn.langya.verify.Verify;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Enable;
import org.union4dev.base.events.movement.MotionUpdateEvent;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.gui.click.ClickGuiScreen;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChestStealer implements Access.InstanceAccess {

    private final NumberValue delay = new NumberValue("Delay", 80, 300, 0, 10);
    private final BooleanValue aura = new BooleanValue("Aura", true);
    private final NumberValue auraRange = new NumberValue("Aura Range", 3, 6, 1, 1);
    public static BooleanValue stealingIndicator = new BooleanValue("Stealing Indicator", false);
    public static BooleanValue titleCheck = new BooleanValue("Title Check", true);
    public static BooleanValue freeLook = new BooleanValue("Free Look", true);
    private final BooleanValue reverse = new BooleanValue("Reverse", true);
    public static final BooleanValue silent = new BooleanValue("Silent", true);
    private final BooleanValue smart = new BooleanValue("Smart", false);

    private final List<BlockPos> openedChests = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private final TimerUtil timer = new TimerUtil();
    public static boolean stealing;
    private boolean clear;


    @EventTarget
    public void onMotionEvent(MotionUpdateEvent e) {
        if (Verify.user != User.User) Access.getInstance().getModuleManager().setEnable(ChestStealer.class,false);
        if (mc.currentScreen instanceof ClickGuiScreen) return;
        if (e.isPre()) {
            if (aura.getValue()) {
                final int radius = auraRange.getValue().intValue();
                for (int x = -radius; x < radius; x++) {
                    for (int y = -radius; y < radius; y++) {
                        for (int z = -radius; z < radius; z++) {
                            final BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                            if (pos.getBlock() == Blocks.chest && !openedChests.contains(pos)) {
                                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, new Vec3(pos))) {
                                    mc.thePlayer.swingItem();
                                    final float[] rotations = RotationUtils.getFacingRotations2(pos.getX(), pos.getY(), pos.getZ());
                                    e.setRotations(rotations[0], rotations[1]);
                                    RotationUtils.setVisualRotations(rotations[0], rotations[1]);
                                    openedChests.add(pos);
                                }
                            }
                        }
                    }
                }
            }
            if (mc.thePlayer.openContainer instanceof ContainerChest) {
                ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
                IInventory chestInv = chest.getLowerChestInventory();
                if (titleCheck.getValue() && (!(chestInv instanceof ContainerLocalMenu) || !((ContainerLocalMenu) chestInv).realChest))
                    return;
                clear = true;

                List<Integer> slots = new ArrayList<>();
                for (int i = 0; i < chestInv.getSizeInventory(); i++) {
                    ItemStack is = chestInv.getStackInSlot(i);
                    if (is != null && (!smart.getValue() || !(InvManager.getInstance().isBadItem(is, -1, true) || items.contains(is.getItem())))) {
                        slots.add(i);
                    }
                }

                if (reverse.getValue()) {
                    Collections.reverse(slots);
                }

                slots.forEach(s -> {
                    ItemStack is = chestInv.getStackInSlot(s);
                    Item item = is != null ? is.getItem() : null;
                    if (item != null && !items.contains(item) && (delay.getValue() == 0 || timer.hasTimeElapsed(delay.getValue().longValue(), true))) {
                        if (smart.getValue() && !(item instanceof ItemBlock)) {
                            items.add(is.getItem());
                        }
                        mc.playerController.windowClick(chest.windowId, s, 0, 1, mc.thePlayer);
                    }
                });

                if (slots.isEmpty() || isInventoryFull()) {
                    items.clear();
                    clear = false;
                    stealing = false;
                    mc.thePlayer.closeScreen();
                }
            } else if (clear) {
                items.clear();
                clear = false;
            }
        }
    }

    @EventTarget
    public void onRender2DEvent(Render2DEvent event) {
        if (Verify.user != User.User) Access.getInstance().getModuleManager().setEnable(ChestStealer.class,false);
        if (mc.currentScreen instanceof ClickGuiScreen) return;
        if (stealingIndicator.getValue() && stealing) {
            ScaledResolution sr = new ScaledResolution(mc);
            FontManager.M18.drawStringWithShadow("§lStealing...", sr.getScaledWidth() / 2.0F - FontManager.M18.getStringWidth("§lStealing...") / 2.0F, sr.getScaledHeight() / 2.0F + 10,-1);
        }
    }

    @Enable
    public void onEnable() {
        if (Verify.user != User.User) Access.getInstance().getModuleManager().setEnable(ChestStealer.class,false);
        openedChests.clear();
    }

    private boolean isInventoryFull() {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean canSteal() {
        if (Access.getInstance().getModuleManager().isEnabled(ChestStealer.class) && mc.currentScreen instanceof GuiChest) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            IInventory chestInv = chest.getLowerChestInventory();
            return !titleCheck.getValue() || (chestInv instanceof ContainerLocalMenu && ((ContainerLocalMenu) chestInv).realChest);
        }
        return false;
    }

    @EventTarget
    public void onWorldEvent(LoadWorldEvent event) {
        openedChests.clear();
    }

}
