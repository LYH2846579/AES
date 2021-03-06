package AES_1;

/**
 * @author LYHstart
 * @create 2021-10-08 16:17
 */

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Miracle Luna on 2019/11/18
 */
public class Md5UtilSimple {

    /**
     * 将数据进行 MD5 加密，并以16进制字符串格式输出
     * @param data
     * @return
     *
     * md5算法中字节数组转换为16进制时存在严重问题！
     */
    public static String md5(String data) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] md5 = md.digest(data.getBytes(StandardCharsets.UTF_8));

            int i = 0;
            // 将字节数据转换为十六进制
            for (byte b : md5) {
                sb.append(Integer.toHexString(b & 0xff));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String password = "passwordshuxbjkcbkwehqbjxbwdkbvqkwdbxjwhwbclkqwhbdx jVCKEW";
        String md5HexStr = md5(password);
        System.out.println("==> MD5 加密前: " + password);
        System.out.println("==> MD5 加密后: " + md5HexStr);
    }
}
