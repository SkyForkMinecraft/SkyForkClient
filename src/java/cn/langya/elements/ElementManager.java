package cn.langya.elements;

import cn.langya.elements.impls.*;
import cn.langya.elements.impls.keystore.KeyStore;
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

    public Element getElement(String name) {
        for (Element element : elements) if(element.getName().equalsIgnoreCase(name)) return element;
        return null;
    }

    public Element getElement(Element element) {
        for (Element e : elements) if (e == element) return element;
        return null;
    }

    void registers() {
        elements.add(new Info());
        elements.add(new TargetHUD());
        elements.add(new KeyStore());
        elements.add(new ComboInfo());
    }

    @EventTarget
    void onR2D(Render2DEvent e) {
        for (Element element : elements) {
            if (!element.isState()) continue; element.update();
        }
    }

}
