package cn.langya.elements;

import lombok.Getter;
import lombok.Setter;
import org.union4dev.base.Access;

/**
 * @author LangYa466
 * @date 2024/4/11 18:16
 */

@Getter
@Setter
public class Element implements Access.InstanceAccess {
    String name;
    float mouseX, mouseY;
    float x, y, moveX, moveY, width, height;
    boolean state, dragging;

    public Element(String name,float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (dragging) {
            this.setX(this.mouseX - this.moveX);
            this.setY(this.mouseY - this.moveY);
        }

        this.draw();
    }

    public void draw() { }

    void toggle() { state = !state; }

    public void updateMousePos(int mouseX, int mouseY) {
        if (dragging) {
            this.mouseX = mouseX;
            this.mouseY = mouseY;
        }
    }
}


