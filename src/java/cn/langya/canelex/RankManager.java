package cn.langya.canelex;

import cn.langya.event.TextEvent;
import org.union4dev.base.annotations.event.EventTarget;

/**
 * @author LangYa
 * @since 2024/6/5 下午9:25
 */

public class RankManager {
    @EventTarget
    public void onT(TextEvent e) {
        // 2582457270 赞助用户 20人民币
        if (e.getText().equalsIgnoreCase("NotChisken")) e.setText("[SponsorShip] NotChisken");
        if (e.getText().equalsIgnoreCase("lindsey614")) e.setText("[SponsorShip] lindsey614");

        // 2696478875 生病龙虾 SickLobster
        if (e.getText().equalsIgnoreCase("UnfairLobster")) e.setText("[Staff] UnfairLobster");
    }
}
