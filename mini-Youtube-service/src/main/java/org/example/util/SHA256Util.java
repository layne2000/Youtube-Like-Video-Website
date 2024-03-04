package org.example.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHA256Util {

    public static String createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesArrayToHexString(salt);
    }

    public static String generateHashedStr(String rawStr, String saltStr) {
        try {
            byte[] salt = hexStringToByteArray(saltStr);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(rawStr.getBytes(StandardCharsets.UTF_8));
            return bytesArrayToHexString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error during string SHA256 hashing", e);
        }
    }

    public static boolean verify(String rawStr, String storedHashedStr, String saltStr) {
        String newHashedStr = generateHashedStr(rawStr, saltStr);
        return newHashedStr.equals(storedHashedStr);
    }

    private static String bytesArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(String.format("%02x", aByte));
        }
        return sb.toString();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    //TODO: to be verified
    public static String getFileSHA256(MultipartFile file) throws Exception {
        // 创建 SHA256 摘要算法实例
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        // 获取文件的字节数组
        byte[] fileBytes = file.getBytes();
        // 更新摘要算法实例的输入
        digest.update(fileBytes);
        // calculate hash values
        byte[] hashBytes = digest.digest();
        // convert hash values to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
