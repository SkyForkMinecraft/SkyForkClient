package cn.yapeteam.cloudmusic.module;

import cn.langya.elements.Element;
import cn.langya.font.FontManager;
import cn.yapeteam.cloudmusic.MusicManager;
import cn.yapeteam.cloudmusic.ui.MusicOverlayRenderer;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.update.TickEvent;
import org.union4dev.base.value.impl.BooleanValue;

public class MusicPlayerOverlay extends Element {
    private final BooleanValue visualize = new BooleanValue("Visualize", true);

    public MusicPlayerOverlay() {
        super(0,0);
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
}