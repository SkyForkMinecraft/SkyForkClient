package com.skyfork.client.value.impl;

import com.skyfork.client.value.AbstractValue;

import java.awt.*;

public class ColorValue extends AbstractValue {
    private Color color;

    public ColorValue(String name, Color value) {
        super(name);
        this.color = value;
    }

    @Override
    public Color getValue() {
        return color;
    }

}
