package com.skyfork.api.waveycapes.config;

import com.skyfork.api.waveycapes.CapeMovement;
import com.skyfork.api.waveycapes.CapeStyle;
import com.skyfork.api.waveycapes.WindMode;

public class Config {
    public static final WindMode windMode = WindMode.NONE;
    public static final CapeStyle capeStyle = CapeStyle.SMOOTH;
    public static final CapeMovement capeMovement = CapeMovement.BASIC_SIMULATION;
    public static final int gravity = 25;
    public static final int heightMultiplier = 6;
}

