package com.skyfork.api.langya.modules.misc;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.annotations.module.Disable;
import com.skyfork.client.annotations.module.Enable;
import com.skyfork.client.events.update.UpdateEvent;
import com.skyfork.client.value.impl.ComboValue;
import com.skyfork.client.value.impl.NumberValue;

public class Fulbright implements Access.InstanceAccess {
    private final ComboValue modeValue = new ComboValue("Mode","伽马值", "夜视药水", "伽马值");
    private final NumberValue gammaValue = new NumberValue("自定义伽马值",1,1,2,0.01);
    private float prevGamma = -1;

    @Enable
    public void onEnable() {
        prevGamma = mc.gameSettings.gammaSetting;
    }

    @Disable
    public void onDisable() {
        if(prevGamma == -1)
            return;

        mc.gameSettings.gammaSetting = prevGamma;
        prevGamma = -1;
        if(mc.thePlayer != null) mc.thePlayer.removePotionEffectClient(Potion.nightVision.id);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        switch (modeValue.getValue().toLowerCase()) {
            case "伽马值":
                    mc.gameSettings.gammaSetting = (gammaValue.getValue().intValue() * 100);
                break;
            case "夜视药水":
                mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 1337, 1));
                break;
        }
    }

    @Disable
    void ond() {
        mc.gameSettings.gammaSetting = prevGamma;
        prevGamma = -1;
    }

}