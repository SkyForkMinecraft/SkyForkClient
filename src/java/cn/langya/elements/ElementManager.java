package cn.langya.elements;

import cn.cedo.render.targethud.TargetHUDMod;
import cn.langya.elements.impls.*;
import cn.superskidder.modules.Combo;
import cn.yapeteam.cloudmusic.module.MusicPlayerOverlay;
import lombok.Getter;
import net.minecraft.client.gui.Gui;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.events.render.ShaderEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author LangYa466
 * @since 2024/4/11 18:32
 */

@Getter
public class ElementManager {
    List<Element> elements;

    public ElementManager() {
        elements = new CopyOnWriteArrayList<>();
        EventManager.register(this);
        this.registers();
    }

    void registers() {
        elements.add(new ClientLogo());
        elements.add(new ArrayList());
        elements.add(new FPSInfo());
        elements.add(new PotionInfo());
        elements.add(new CPSInfo());
        elements.add(new MusicPlayerOverlay());
        elements.add(new Combo());
        elements.add(new KeyStore());
        // elements.add(new TargetHUDMod());
    }

    @EventTarget
    void onR2D(Render2DEvent e) {
        for (Element element : elements) element.update(false);
    }

    @EventTarget
    void onS(ShaderEvent e) {
        for (Element element : elements) element.update(true);
    }

}