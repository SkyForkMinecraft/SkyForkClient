package com.skyfork.api.yapeteam.cloudmusic.module;

import com.skyfork.client.value.impl.BooleanValue;

public class MusicPlayerOverlay {
    private final BooleanValue visualize = new BooleanValue("Visualize", true);

    /*
    public MusicPlayerOverlay() {
        setWH(150,30);
        // super("MusicPlayerOverlay", Keyboard.KEY_NONE, Category.Overlay, 0, 0, 150, 30, "free", "free");
    }

    @Override
    public void draw() {
         MusicOverlayRenderer.INSTANCE.renderOverlay(x, y);
    }

    @EventTarget
    private void onTick(TickEvent e) {
        MusicManager.INSTANCE.visualize = visualize.getValue();
        if (MusicOverlayRenderer.INSTANCE.getDisplay() != null)
            width = Math.max(FontManager.M18.getStringWidth(MusicOverlayRenderer.INSTANCE.getDisplay()[0]), FontManager.M18.getStringWidth(MusicOverlayRenderer.INSTANCE.getDisplay()[1])) + 33;
    }

     */
}