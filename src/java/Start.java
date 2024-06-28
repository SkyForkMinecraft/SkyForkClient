import net.minecraft.client.main.Main;

import java.io.File;
import java.util.Arrays;

public enum Start {
    ;

    public static void main(String[] args)
    {
        // System.setProperty("org.lwjgl.librarypath", new File("C:\\SkyForkLauncher\\run\\natives").getAbsolutePath());
        Main.main(concat(new String[] {"--version", "mcp","--username","请去管理页面切换账号", "--accessToken", "0","--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
        // Main.main(concat(new String[] { "--version", "mcp","--username","zdshkasopd" ,"--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}" }, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
