//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Administrator\Downloads\Minecraft1.12.2 Mappings"!

//Decompiled by Procyon!

package com.skyfork.api.dxg.hytprotocol.inv;

import com.skyfork.api.dxg.hytprotocol.Sender;
import com.skyfork.api.dxg.hytprotocol.metadata.PartyMetadata;
import com.skyfork.api.dxg.hytprotocol.metadata.VexViewMetadata;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;

public class HytPartyInviteGui extends GuiContainer
{
    private static final ResourceLocation CHEST_GUI_TEXTURE;
    public static Item back;
    public static Item requestMaterial;
    private ArrayList<PartyMetadata> requests;
    
    public HytPartyInviteGui(final ArrayList<PartyMetadata> requests) {
        super((Container)new HytPartyInviteContainer(requests));
        this.requests = new ArrayList<PartyMetadata>();
        this.requests = requests;
    }
    
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        final int cut = 89;
        final int off = 33;
        this.fontRendererObj.drawString("\u62c9\u4eba\u4e0a\u6302\u8f66", i + 8, j + off + 6, 4210752);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(HytPartyInviteGui.CHEST_GUI_TEXTURE);
        this.drawTexturedModalRect(i, j + off, 0, 0, this.xSize, 35);
        this.drawTexturedModalRect(i, j + 18 + 17 + off, 0, 126 + cut, this.xSize, 96 - cut);
    }
    
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        final Slot slot = this.getSlotAtPosition(mouseX, mouseY);
        if (slot != null && slot.getHasStack()) {
            final ItemStack clickedStack = slot.getStack();
            final Item item = clickedStack.getItem();
            if (item.equals(HytPartyInviteGui.requestMaterial)) {
                VexViewMetadata operation = null;
                if (mouseButton == 0) {
                    operation = new VexViewMetadata("Accept", clickedStack.getTagCompound().getString("AcceptId"));
                }
                else {
                    operation = new VexViewMetadata("Deny", clickedStack.getTagCompound().getString("DenyId"));
                }
                Sender.sendButtonClicked(operation.id);
                this.requests.remove(clickedStack.getTagCompound().getInteger("RequestIndex"));
            }
            if (item.equals(HytPartyInviteGui.back)) {
                this.mc.thePlayer.sendChatMessage("/kh");
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    protected void handleMouseClick(final Slot slotIn, final int slotId, final int clickedButton, final int clickType) {
        if (clickType == 4) {
            return;
        }
        super.handleMouseClick(slotIn, slotId, clickedButton, clickType);
    }
    
    static {
        CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
        HytPartyInviteGui.back = Items.iron_door;
        HytPartyInviteGui.requestMaterial = Items.skull;
    }
    
    private static class HytPartyInviteContainer extends Container
    {
        private IInventory inventory;
        private ArrayList<Slot> slots;
        private ArrayList<PartyMetadata> requests;
        
        public HytPartyInviteContainer(final ArrayList<PartyMetadata> requests) {
            this.slots = new ArrayList<Slot>();
            this.requests = new ArrayList<PartyMetadata>();
            this.requests = requests;
            this.inventory = (IInventory)new InventoryBasic("HytPartyInviteContainer", false, 9);
            final int playerInventoryStartX = 8;
            final int playerInventoryStartY = 51;
            for (int j = 0; j < 9; ++j) {
                final Slot slot = new Slot(this.inventory, j, playerInventoryStartX + j * 18, playerInventoryStartY);
                this.slots.add(slot);
                this.addSlotToContainer(slot);
            }
            final ItemStack backitem = new ItemStack(HytPartyInviteGui.back);
            backitem.setStackDisplayName("\u56de\u53bb");
            backitem.canItalic = false;
            this.slots.get(8).putStack(backitem);
            for (int i = 0; i < 9; ++i) {
                if (i < requests.size()) {
                    final ItemStack requestItem = new ItemStack(HytPartyInviteGui.requestMaterial);
                    requestItem.setStackDisplayName(requests.get(i).getName());
                    requestItem.canItalic = false;
                    requestItem.setTagInfo("AcceptId", (NBTBase)new NBTTagString(requests.get(i).getAcceptId()));
                    requestItem.setTagInfo("DenyId", (NBTBase)new NBTTagString(requests.get(i).getDenyId()));
                    requestItem.setTagInfo("RequestIndex", (NBTBase)new NBTTagInt(i));
                    requestItem.addEnchantment(Enchantment.unbreaking, 1);
                    requestItem.setTagInfo("display", (NBTBase)new NBTTagString("sb"));
                    this.slots.get(i).putStack(requestItem);
                }
            }
        }
        
        public boolean canInteractWith(final EntityPlayer playerIn) {
            return true;
        }
    }
}
