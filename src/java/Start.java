import net.minecraft.client.main.Main;

import java.util.Arrays;

public enum Start {
    ;

    public static void main(String[] args)
    {
                Main.main(concat(new String[] {"--version", "mcp","--username","Lang7a", "--accessToken", "eyJraWQiOiJhYzg0YSIsImFsZyI6IkhTMjU2In0.eyJ4dWlkIjoiMjUzNTQwNTg0MDM3NTU4MyIsImFnZyI6IkFkdWx0Iiwic3ViIjoiNzFkZmUyODYtZjAyMi00ZjE2LTliMzUtMGFhNTE2MTgzOGY0IiwiYXV0aCI6IlhCT1giLCJucyI6ImRlZmF1bHQiLCJyb2xlcyI6W10sImlzcyI6ImF1dGhlbnRpY2F0aW9uIiwiZmxhZ3MiOlsidHdvZmFjdG9yYXV0aCIsIm1zYW1pZ3JhdGlvbl9zdGFnZTQiLCJvcmRlcnNfMjAyMiIsIm11bHRpcGxheWVyIl0sInByb2ZpbGVzIjp7Im1jIjoiNzlhMDFlZDctY2QwMS00ZTI1LWEyYzQtZjRlOWUyYmQ5NmJlIn0sInBsYXRmb3JtIjoiVU5LTk9XTiIsIm5iZiI6MTcxNjk3NTcyMSwiZXhwIjoxNzE3MDYyMTIxLCJpYXQiOjE3MTY5NzU3MjF9.hZnccBOfIkiPx9TPvkuLK1MZUKF9CqxW4hBIwSZ-1Fc","--uuid","79a01ed7cd014e25a2c4f4e9e2bd96be","--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
        // Main.main(concat(new String[] { "--version", "mcp","--username","zdshkasopd" ,"--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}" }, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
