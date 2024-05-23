package cn.langya.elements;

import cn.langya.elements.impls.*;
import lombok.Getter;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.events.render.Render2DEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author LangYa466
 * @date 2024/4/11 18:32
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
    }

    @EventTarget
    void onR2D(Render2DEvent e) {
        for (Element element : elements) element.update();
    }

}