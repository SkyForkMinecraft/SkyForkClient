package cn.langya.elements.impls;

import cn.langya.elements.Element;
import lombok.Getter;
import net.minecraft.entity.Entity;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.events.misc.AttackEvent;
import skid.cedo.shader.RoundedUtil;

import java.awt.*;

/**
 * @author LangYa466
 * @date 2024/4/21 22:08
 */

@Getter
public class ComboInfo extends Element {

    Entity tempAttackEntity;
    int combo;

    public ComboInfo() {
        super("ComboInfo",500, 500);
        EventManager.register(this);
    }

    @EventTarget
    void onA(AttackEvent e) {
        if (tempAttackEntity == null) tempAttackEntity = e.getTarget();

        new Thread(() -> {

            if (tempAttackEntity != null && mc.thePlayer.hurtTime < 1 && tempAttackEntity == e.getTarget()) {
                combo += 1;

                // 考虑到tick
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {}

            } else if (tempAttackEntity != null && tempAttackEntity != e.getTarget()) {
                combo = 0;
                tempAttackEntity = e.getTarget();
            }
        }).start();


    }

    @Override
    public void draw() {
        if (mc.thePlayer == null) return;

        if (combo > 0 && mc.thePlayer.hurtTime > 0) combo = 0;

        setWidth(Access.getInstance().getFontManager().F18.getWidth("Combo:" + combo));
        setHeight(Access.getInstance().getFontManager().F18.getHeight());

        // draw
        RoundedUtil.drawRound(getX(), getY(), getWidth(), getHeight(), 3, new Color(0, 0, 0, 80));
        Access.getInstance().getFontManager().F18.drawStringWithShadow("Combo:" + combo, getX() + 0.2, getY() + 0.2, -1);
    }
}
