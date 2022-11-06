package com.eticket.infrastructure.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class EncryptionUtil {
    private static final Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);
    @Value("${eticket.app.salt}")
    private static final String SALT = "rajblowplast";
    private static final byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    @Value("${eticket.app.jwtSecret}")
    private static String SECRET_KEY = "hsl43=m/mdYo87%fesSYm2";

    public static String encrypt(String plainText) {
        return plainText;
//        try {
//            IvParameterSpec ivspec = new IvParameterSpec(iv);
//
//            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
//            SecretKey tmp = factory.generateSecret(spec);
//            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
//            return Base64.getEncoder()
//                    .encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
//        } catch (Exception e) {
//            logger.error("Error while encrypting: {}", e.getMessage());
//        }
//        return null;
    }

    public static String decrypt(String strToDecrypt) {
//        try {
//            IvParameterSpec ivspec = new IvParameterSpec(iv);
//
//            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
//            SecretKey tmp = factory.generateSecret(spec);
//            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
//            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
//        } catch (Exception e) {
//            logger.error("Error while decrypting: {}", e.getMessage());
//        }
//        return null;
        return strToDecrypt;
    }
}
