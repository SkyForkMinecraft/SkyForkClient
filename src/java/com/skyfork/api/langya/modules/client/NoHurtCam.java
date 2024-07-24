package com.skyfork.api.langya.modules.client;

import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.render.HurtCamEvent;

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
