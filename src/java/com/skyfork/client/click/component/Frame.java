package com.skyfork.client.gui.click.component;

import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.client.Access;
import com.skyfork.client.gui.click.component.components.Button;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.client.module.Category;

import java.awt.*;
import java.util.ArrayList;


public class Frame {

    public ArrayList<Component> components;
    public Category category;
    private boolean open;
    private final int width;
    private int y;
    private int x;
    private final int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;

    public Frame(Category cat) {
        this.components = new ArrayList<Component>();
        this.category = cat;
        this.width = 88;
        this.x = 5;
        this.y = 5;
        this.barHeight = 13;
        this.dragX = 0;
        this.open = false;
        this.isDragging = false;
        int tY = this.barHeight;
        for (Class<?> mod : Access.getInstance().getModuleManager().getModulesByCategory(category)) {
            if (Access.getInstance().getModuleManager().getModulesByCategory(category).get(Access.getInstance().getModuleManager().getModulesByCategory(category).size() - 1) == mod) {
                //应该是true但是render有问题暂时false
                Button modButton = new Button(mod, this, tY,false);
                this.components.add(modButton);
            } else {
                Button modButton = new Button(mod, this, tY,false);
                this.components.add(modButton);
            }
            tY += 12;
        }
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void renderFrame() {

        RoundedUtil.drawRound(x + 0.7F, this.y + 3.9F, 86.5F, 11,3, new Color(27, 30, 30));

        FontManager.M18.drawStringWithShadow(this.category.getName(), (this.x + 2)  + 4, (this.y + 4f) , 0xFFFFFFFF);
        FontManager.M18.drawStringWithShadow(this.open ? "-" : "+", (this.x + this.width - 10)  + 3, (this.y + 4.5f) , -1);
        if (this.open) {
            if (!this.components.isEmpty()) {
                for (Component component : components) {
                    component.renderComponent();
                }
            }
        }

    }

    public void refresh() {
        int off = this.barHeight;
        for (Component comp : components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - dragX);
            this.setY(mouseY - dragY);
        }
    }

    public boolean isWithinHeader(int x, int y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
	}

}
