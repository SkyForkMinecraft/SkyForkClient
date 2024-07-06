
package net.minecraft.client.gui;

import cn.cedo.animations.Animation;
import cn.cedo.animations.Direction;
import cn.cedo.animations.impl.DecelerateAnimation;
import cn.langya.elements.Element;
import cn.langya.font.FontManager;
import cn.langya.utils.AnimationUtil;
import cn.langya.utils.MouseUtil;
import com.google.common.collect.Lists;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.union4dev.base.Access;
import cn.cedo.render.RenderUtil;
import cn.cedo.shader.RoundedUtil;

public class GuiChat extends GuiScreen
{
    private String historyBuffer = "";
    private int sentHistoryCursor = -1;
    private boolean playerNamesFound;
    private boolean waitingOnAutocomplete;
    private int autocompleteIndex;
    private List<String> foundPlayerNames = Lists.<String>newArrayList();
    protected GuiTextField inputField;
    private String defaultInputFieldText = "";
    private Element element;

    public GuiChat()
    {
    }

    public GuiChat(String defaultText)
    {
        this.defaultInputFieldText = defaultText;
    }

    public void initGui()
    {
        openingAnimation = new DecelerateAnimation(175, 1);
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        this.inputField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
        this.inputField.setMaxStringLength(100);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (state == 0) {
            if (this.element != null) {
                this.element.setDragging(false);
                this.element = null;
            }
        }
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }

    public void updateScreen()
    {
        this.inputField.updateCursorCounter();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.waitingOnAutocomplete = false;

        if (keyCode == 15)
        {
            this.autocompletePlayerNames();
        }
        else
        {
            this.playerNamesFound = false;
        }

        if (keyCode == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (keyCode != 28 && keyCode != 156)
        {
            if (keyCode == 200)
            {
                this.getSentHistory(-1);
            }
            else if (keyCode == 208)
            {
                this.getSentHistory(1);
            }
            else if (keyCode == 201)
            {
                this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
            }
            else if (keyCode == 209)
            {
                this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
            }
            else
            {
                this.inputField.textboxKeyTyped(typedChar, keyCode);
            }
        }
        else
        {
            String s = this.inputField.getText().trim();

            if (s.length() > 0)
            {
                this.sendChatMessage(s);
            }

            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            if (i > 1)
            {
                i = 1;
            }

            if (i < -1)
            {
                i = -1;
            }

            if (!isShiftKeyDown())
            {
                i *= 7;
            }

            this.mc.ingameGUI.getChatGUI().scroll(i);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        for (Element element : Access.getInstance().getElementManager().getElements()) {
            if (MouseUtil.isHovering(element.getX(), element.getY(), element.getWidth(), element.getHeight(), mouseX, mouseY)) {

                element.setDragging(true);
                this.element = element;

                if (mouseButton == 0) {
                    element.setMoveX(mouseX - element.getX());
                    element.setMoveY(mouseY - element.getY());
                } else {
                    element.setDragging(false);
                    this.element = null;
                }
            }
        }

        IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

        if (this.handleComponentClick(ichatcomponent)) {
            return;
        }

        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void setText(String newChatText, boolean shouldOverwrite)
    {
        if (shouldOverwrite)
        {
            this.inputField.setText(newChatText);
        }
        else
        {
            this.inputField.writeText(newChatText);
        }
    }

    public void autocompletePlayerNames()
    {
        if (this.playerNamesFound)
        {
            this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());

            if (this.autocompleteIndex >= this.foundPlayerNames.size())
            {
                this.autocompleteIndex = 0;
            }
        }
        else
        {
            int i = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
            this.foundPlayerNames.clear();
            this.autocompleteIndex = 0;
            String s = this.inputField.getText().substring(i).toLowerCase();
            String s1 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
            this.sendAutocompleteRequest(s1, s);

            if (this.foundPlayerNames.isEmpty())
            {
                return;
            }

            this.playerNamesFound = true;
            this.inputField.deleteFromCursor(i - this.inputField.getCursorPosition());
        }

        if (this.foundPlayerNames.size() > 1)
        {
            StringBuilder stringbuilder = new StringBuilder();

            for (String s2 : this.foundPlayerNames)
            {
                if (stringbuilder.length() > 0)
                {
                    stringbuilder.append(", ");
                }

                stringbuilder.append(s2);
            }

            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
        }

        this.inputField.writeText((String)this.foundPlayerNames.get(this.autocompleteIndex++));
    }

    private void sendAutocompleteRequest(String p_146405_1_, String p_146405_2_)
    {
        if (p_146405_1_.length() >= 1)
        {
            BlockPos blockpos = null;

            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                blockpos = this.mc.objectMouseOver.getBlockPos();
            }

            this.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_, blockpos));
            this.waitingOnAutocomplete = true;
        }
    }

    public void getSentHistory(int msgPos)
    {
        int i = this.sentHistoryCursor + msgPos;
        int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        i = MathHelper.clamp_int(i, 0, j);

        if (i != this.sentHistoryCursor)
        {
            if (i == j)
            {
                this.sentHistoryCursor = j;
                this.inputField.setText(this.historyBuffer);
            }
            else
            {
                if (this.sentHistoryCursor == j)
                {
                    this.historyBuffer = this.inputField.getText();
                }

                this.inputField.setText((String)this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
                this.sentHistoryCursor = i;
            }
        }
    }

    public static Animation openingAnimation = new DecelerateAnimation(175, 1, Direction.BACKWARDS);

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (openingAnimation.finished(Direction.BACKWARDS)) {
            mc.displayGuiScreen((GuiScreen) null);
            return;
        }
        RoundedUtil.drawRound(0,  this.height - (14 * openingAnimation.getOutput().floatValue()), this.width, this.height - 4,5, new Color(0,0,0,150));

        inputField.yPosition = this.height - (12 * openingAnimation.getOutput().intValue());
        this.inputField.drawTextBox();
        IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

        if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null)
        {
            this.handleComponentHover(ichatcomponent, mouseX, mouseY);
        }

        if (this.element != null) element.updateMousePos(mouseX, mouseY);

        for (Element element : Access.getInstance().getElementManager().getElements()) {
            if (MouseUtil.isHovering(element.getX(), element.getY(), element.getWidth(), element.getHeight(), mouseX, mouseY)) {

                RenderUtil.drawBorder(element.getX() - 4, element.getY() - 4,  element.getWidth() + 8, element.getHeight() + 8, 1, -1);
                FontManager.M14.drawCenteredStringWithShadow(String.format("X轴: %s Y轴: %s", element.getX(), element.getY()), element.getX() + 37, element.getY() - 12, -1);
            }
            /*
            else if(MouseUtil.isHovering(element.getX(), element.getY(), element.getWidth() + 60, element.getHeight() + 60, mouseX, mouseY)) {
                ClientButton button = new ClientButton(element.getX() + 60, element.getY(), 60, 30, 3, "删除");
                button.setMouseX(mouseX);
                button.setMouseY(mouseY);
                button.draw();
                if (keyCode == 0 && button.isHover()) element.setState(false);
            }

             */
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void onAutocompleteResponse(String[] p_146406_1_)
    {
        if (this.waitingOnAutocomplete)
        {
            this.playerNamesFound = false;
            this.foundPlayerNames.clear();

            for (String s : p_146406_1_)
            {
                if (s.length() > 0)
                {
                    this.foundPlayerNames.add(s);
                }
            }

            String s1 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
            String s2 = StringUtils.getCommonPrefix(p_146406_1_);

            if (s2.length() > 0 && !s1.equalsIgnoreCase(s2))
            {
                this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
                this.inputField.writeText(s2);
            }
            else if (this.foundPlayerNames.size() > 0)
            {
                this.playerNamesFound = true;
                this.autocompletePlayerNames();
            }
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
