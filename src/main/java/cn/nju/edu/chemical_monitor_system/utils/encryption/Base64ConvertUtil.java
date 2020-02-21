package cn.nju.edu.chemical_monitor_system.utils.encryption;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64ConvertUtil {

    private Base64ConvertUtil() {
    }

    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));
        return new String(encodeBytes);
    }

    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
        return new String(decodeBytes);
    }

}
