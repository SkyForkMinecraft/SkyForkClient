package com.skyfork.api.yapeteam.ClickUI.component;

public interface Component {
    void draw(float x, float y, float mouseX, float mouseY);

    void mouseClicked(int mouseX, int mouseY, int mouseButton);

    void mouseReleased(float mouseX, float mouseY, int state);
}
