package cn.langya;

import anti_leak.Native;
import cn.langya.event.TextEvent;
import net.minecraft.util.EnumChatFormatting;
import org.union4dev.base.annotations.event.EventTarget;
import unknow.IoUtil;
import unknow.WebUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author LangYa
 * @since 2024/6/5 下午9:25
 */

public class RankManager {
    public static final String PRIMARY_COLOR = EnumChatFormatting.RED.toString();
    public static final String SECONDARY_COLOR = EnumChatFormatting.GRAY.toString();

    public static final List<String> adminList = new CopyOnWriteArrayList<>();

    public RankManager() {
        BufferedReader br = null;
        try {
            br = IoUtil.StringToBufferedReader(WebUtils.get("https://skyfork.cn/rank.php").replaceAll("<br />",""));
            String line;
            for (line = br.readLine(); line != null; line = br.readLine()) {
                String[] tokens = line.split("-");
                String userName = tokens[0];
                String type = tokens[1];
                RankUtil.tokens.put(userName,type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Native
    @EventTarget
    public void onT(TextEvent e) {
        RankUtil.tokens.forEach((playerName, rank) -> set(e, playerName, rank));
    }

    private String getRank(String str, String color) {
        return SECONDARY_COLOR + "[" + color + EnumChatFormatting.BOLD + str + SECONDARY_COLOR + "] ";
    }

    private void set(TextEvent e,String playerName,String rank) {
        boolean set = false;
        if (e.text.contains(playerName) && !set) {
            if (rank.equals("Admin")) {
                adminList.add(playerName);
                e.text = String.format("%s ", getRank(rank,PRIMARY_COLOR)) + e.text;
            }  else {
                e.text = String.format("%s ", getRank(rank,EnumChatFormatting.BLUE.toString())) + e.text;
            }
            set = true;
        }
    }

}
