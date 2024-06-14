package cn.langya.elements;

import lombok.Getter;
import lombok.Setter;
import org.union4dev.base.Access;

/**
 * @author LangYa466
 * @since 2024/4/11 18:16
 */

@Getter
@Setter
public class Element implements Access.InstanceAccess {
    float mouseX, mouseY;
    public float x, y, moveX, moveY, width, height;
    boolean dragging;

    public Element(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setWH(float width,float height) {
        this.width = width;
        this.height = height;
    }

    public void update() {
        if (dragging) {
            this.setX(this.mouseX - this.moveX);
            this.setY(this.mouseY - this.moveY);
        }

        this.draw();
    }

    public void draw() { }

    public void updateMousePos(int mouseX, int mouseY) {
        if (dragging) {
            this.mouseX = mouseX;
            this.mouseY = mouseY;
        }
    }
}