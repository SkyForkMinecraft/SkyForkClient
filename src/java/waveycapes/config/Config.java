package waveycapes.config;

import waveycapes.CapeMovement;
import waveycapes.CapeStyle;
import waveycapes.WindMode;

public class Config {
    public static final WindMode windMode = WindMode.NONE;
    public static final CapeStyle capeStyle = CapeStyle.SMOOTH;
    public static final CapeMovement capeMovement = CapeMovement.BASIC_SIMULATION;
    public static final int gravity = 25;
    public static final int heightMultiplier = 6;
}

