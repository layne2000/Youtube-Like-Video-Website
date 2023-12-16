package org.example.util;

import org.apache.commons.codec.digest.DigestUtils;
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

    //    //获取文件md5加密后的字符串 // TODO: modify it later
//    public static String getFileMD5(MultipartFile file) throws Exception {
//        InputStream fis = file.getInputStream();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int byteRead;
//        while((byteRead = fis.read(buffer)) > 0){
//            baos.write(buffer, 0, byteRead);
//        }
//        fis.close();
//        return DigestUtils.md5Hex(baos.toByteArray());
//    }

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

}
