package com.skyfork.client.gui.click.component.components.sub;

import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import com.skyfork.client.Access;
import com.skyfork.client.gui.click.component.Component;
import com.skyfork.client.gui.click.component.components.Button;
import com.skyfork.api.langya.font.FontManager;

public class VisibleButton extends Component {

    private boolean hovered;
    private final Button parent;
    private int offset;
    private int x;
    private int y;
    private final Class<?> mod;

    public VisibleButton(Button button, Class<?> mod, int offset) {
        this.parent = button;
        this.mod = mod;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
    }

    @Override
    public void renderComponent() {
        Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ? 0xFF222222 : 0xFF111111);
        Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, 0xFF111111);
        GL11.glPushMatrix();

        String s = Access.getInstance().getModuleManager().isVisible(mod) ? "可见" : "不可见";
        FontManager.M18.drawStringWithShadow("列表显示: " + s, (parent.parent.getX() + 7) , (parent.parent.getY() + offset + 2)  +1, -1);
        GL11.glPopMatrix();
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            Access.getInstance().getModuleManager().setVisible(mod, !Access.getInstance().getModuleManager().isVisible(mod));
        }
    }

    public boolean isMouseOnButton(int x, int y) {
		return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
	}
}