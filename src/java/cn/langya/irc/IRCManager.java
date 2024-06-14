package cn.langya.irc;

import com.yumegod.obfuscation.Native;
import lombok.Getter;
import lombok.SneakyThrows;
import org.union4dev.base.util.ChatUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Native
public class IRCManager {
    @Getter
    private Socket client;

    @SneakyThrows
    public IRCManager() {


    }


}
