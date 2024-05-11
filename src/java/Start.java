import net.minecraft.client.main.Main;

import java.util.Arrays;

public enum Start {
    ;

    public static void main(String[] args)
    {
        Main.main(concat(new String[] {"--version", "mcp","--username","LangYa466", "--accessToken", "eyJraWQiOiJhYzg0YSIsImFsZyI6IkhTMjU2In0.eyJ4dWlkIjoiMjUzNTQ1MzkzNTQyNzgzNSIsImFnZyI6IkFkdWx0Iiwic3ViIjoiYzkxMzc0NTMtNjRiNS00M2MwLThmNzYtNzIzMmQwYzg1MzA2IiwiYXV0aCI6IlhCT1giLCJucyI6ImRlZmF1bHQiLCJyb2xlcyI6W10sImlzcyI6ImF1dGhlbnRpY2F0aW9uIiwiZmxhZ3MiOlsidHdvZmFjdG9yYXV0aCIsIm1zYW1pZ3JhdGlvbl9zdGFnZTQiLCJvcmRlcnNfMjAyMiIsIm11bHRpcGxheWVyIl0sInByb2ZpbGVzIjp7Im1jIjoiOTkyNDY5ODAtZGY4MC00NWU5LWE4MGYtYTUyMjlkOWVkMTYwIn0sInBsYXRmb3JtIjoiVU5LTk9XTiIsInl1aWQiOiJlZjlhZDdkY2M1NmU3Y2NiYjk3NWYzNzI0OGU3OWFmOCIsIm5iZiI6MTcxMzUxNzI3OSwiZXhwIjoxNzEzNjAzNjc5LCJpYXQiOjE3MTM1MTcyNzl9.3i3I2CG_cFwZ4qknqK60Ic0WyYAXeN5OhJJSa80lmro","--uuid","99246980df8045e9a80fa5229d9ed160","--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
