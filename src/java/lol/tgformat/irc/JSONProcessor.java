package lol.tgformat.irc;

import cn.langya.utils.EncryptUtil;
import org.union4dev.base.util.ChatUtil;

/**
 * @author TG_format
 * @since 2024/5/25 14:16
 */
public class JSONProcessor {
    private ReceiveData data;
    public JSONProcessor(ReceiveData data) {
        this.data = data;
    }
    public void process() {
        switch (data.getType()) {
            case "msg":
                System.out.println(data.getData());
                ChatUtil.info(" [IRC] : " + data.getData());
                break;
            case "UpdateIGN":
                InGameUsers.users.add(data.getData());
                break;
            case "Welcome":
                ChatUtil.success(" [IRC] : " + data.getData());
                break;
        }
    }
}
