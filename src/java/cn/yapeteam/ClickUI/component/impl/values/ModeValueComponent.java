package cn.yapeteam.ClickUI.component.impl.values;

import cn.cedo.shader.RoundedUtil;
import cn.langya.font.FontManager;
import cn.yapeteam.ClickUI.ClickUIScreen;
import cn.yapeteam.ClickUI.component.Component;
import org.union4dev.base.value.impl.ComboValue;

import java.awt.*;

public class ModeValueComponent implements Component {
    ComboValue value;
    float x, y;
    int modeIndex;

    public ModeValueComponent(ComboValue setting) {
        this.value = setting;
        modeIndex = 0;
    }

    private Color setAlpha(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), ClickUIScreen.globalAlpha);
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        this.x = x;
        this.y = y;
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        float width = ClickUIScreen.rightWidth - 10f;
        RoundedUtil.drawRound(lx, y, width, 15, 3, setAlpha(new Color(222, 222, 222)));
        FontManager.M18.drawString(value.getName() + ":" + value.getValue(), lx + (width - FontManager.M18.getStringWidth(value.getName() + ":" + value.getValue())) / 2f, y + 4, setAlpha(new Color(66, 66, 66)).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        float width = ClickUIScreen.rightWidth - 10f;
        if (isHovered(lx + 5, y, width, 15, mouseX, mouseY)) {
            int maxIndex = value.getStrings().length;
            if (modeIndex + 1 >= maxIndex)
                modeIndex = 0;
            else
                modeIndex++;
            value.setValue(value.getStrings()[modeIndex]);
        }
    }

    public boolean isHovered(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int state) {

    }
}
