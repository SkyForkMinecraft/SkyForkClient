package cn.langya.modules.client;

import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.HurtCamEvent;

/**
 * @author LangYa466
 * @since 2024/5/11 22:04
 */

public class NoHurtCam {

    @EventTarget
    public void onHurtCam(HurtCamEvent event) {
        event.setCancelled(true);
    }

}
