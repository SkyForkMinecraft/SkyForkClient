package com.skyfork.api.langya.verify;

import com.yumegod.obfuscation.Native;
import lombok.SneakyThrows;
import com.skyfork.api.unknow.WebUtils;

import java.awt.*;
import java.net.URL;
import java.security.MessageDigest;

import static com.skyfork.client.Access.*;

/**
 * @author LangYa466
 * @since 2024/5/6 20:10
 */

public class Verify {

    public static User user = User.Free;

    public static String getHWID() {
        try{
            String toEncrypt =  System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            StringBuffer hexString = new StringBuffer();

            byte byteData[] = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }


    @Native
    @SneakyThrows
    public static void verify() {

        if (WebUtils.get("https://gitee.com/langya1337/skyfork/raw/master/hwid.txt").contains(getHWID())) {
            user = User.User;
            displayTray(CLIENT_NAME,"已切换为付费模式", TrayIcon.MessageType.INFO);
        } else {
            user = User.Free;
            displayTray(CLIENT_NAME,"未在服务器找到获取到您的密钥，已切换为免费模式，需要付费请在主页面获取HWID发送给群主并支付指定的费用", TrayIcon.MessageType.INFO);
        }
    }

}
