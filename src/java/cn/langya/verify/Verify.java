package cn.langya.verify;

import lombok.SneakyThrows;
import unknow.WebUtils;

import java.awt.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

import static org.union4dev.base.Access.*;

/**
 * @author LangYa466
 * @date 2024/5/6 20:10
 */

public class Verify {

    public static User user = User.Free;

    public static String getHWID() {
        return String.valueOf(Base64.getEncoder().encode((System.getenv("USERNAME") + System.getenv("COMPUTERNAME") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("NUMBER_OF_PROCESSORS")).getBytes(StandardCharsets.UTF_8)));
    }

    @SneakyThrows
    public static String getToken() {
        String hwid = String.valueOf(Base64.getEncoder().encode((System.getenv("USERNAME") + System.getenv("COMPUTERNAME") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("NUMBER_OF_PROCESSORS")).getBytes(StandardCharsets.UTF_8)));
        Scanner scanner = new Scanner(WebUtils.get("https://skyclient.lol/%E5%93%A5%E6%88%91%E5%B0%B1%E4%B8%80%E5%85%8D%E8%B4%B9%E5%AE%A2%E6%88%B7%E7%AB%AF%E5%88%AB%E7%A0%B4%E8%A7%A3%E4%BA%86.txt"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains(hwid)) {
                return line;
            }
        }
        return null;
    }

    @SneakyThrows
    public static void verify() {
        if (!WebUtils.get("https://skyclient.lol/%E5%93%A5%E6%88%91%E5%B0%B1%E4%B8%80%E5%85%8D%E8%B4%B9%E5%AE%A2%E6%88%B7%E7%AB%AF%E5%88%AB%E7%A0%B4%E8%A7%A3%E4%BA%862.txt").contains(CLIENT_VERSION)) {
            displayTray(CLIENT_NAME,"您的客户端版本过低，请更新到最新版本！", TrayIcon.MessageType.ERROR);
            Desktop.getDesktop().browse(new URL("https://qm.qq.com/q/qH7jTDrJcI").toURI());
        }

        if(getToken() != null) {
            user = User.User;
            displayTray(CLIENT_NAME,"已切换为付费模式", TrayIcon.MessageType.INFO);
        } else {
            user = User.Free;
            displayTray(CLIENT_NAME,"未在服务器找到获取到您的密钥，已切换为免费模式，需要付费请在主页面获取HWID发送给群主并支付指定的费用", TrayIcon.MessageType.INFO);
        }
    }

}
