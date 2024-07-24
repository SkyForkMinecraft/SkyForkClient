package com.skyfork.api.cedo.misc;

import lombok.Getter;
import com.skyfork.client.module.render.HUD;
import com.skyfork.client.value.impl.ColorValue;
import com.skyfork.client.value.impl.ComboValue;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class GradientColorWheel {
    private Color color1, color2, color3, color4;
    private ComboValue colorMode;
    private ColorValue colorSetting;

    public GradientColorWheel() {
        color1 = Color.BLACK;
        color2 = Color.BLACK;
        color3 = Color.BLACK;
        color4 = Color.BLACK;
    }

    public ComboValue createModeSetting(String name, String... extraModes) {
        List<String> modesList = new ArrayList<>();
        modesList.add("Sync");
        modesList.add("Custom");
        modesList.addAll(Arrays.asList(extraModes));

        colorMode = new ComboValue(name, "Sync", modesList.toArray(new String[0]));
        colorSetting = new ColorValue("Custom Color", Color.PINK);
        return colorMode;
    }

    public void setColorsForMode(String mode, Color color) {
        setColorsForMode(mode, color, color, color, color);
    }


    public void setColorsForMode(String mode, Color color1, Color color2, Color color3, Color color4) {
        if (colorMode.isMode(mode)) {
            this.color1 = color1;
            this.color2 = color2;
            this.color3 = color3;
            this.color4 = color4;
        }
    }


}