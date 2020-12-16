package com.drajer.ecrapp.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AESEncryption {

  private static final Logger logger = LoggerFactory.getLogger(AESEncryption.class);

  private static SecretKeySpec secretKey;
  private static byte[] key;

  private static byte[] iv = new SecureRandom().generateSeed(16);

  private static IvParameterSpec ivSpec = new IvParameterSpec(iv);

  @Autowired private Environment environment;

  private static String secret;

  public static void setKey(String myKey) {
    MessageDigest sha = null;
    try {
      key = myKey.getBytes("UTF-8");
      sha = MessageDigest.getInstance("SHA-256");
      key = sha.digest(key);
      key = Arrays.copyOf(key, 16);
      secretKey = new SecretKeySpec(key, "AES");
    } catch (NoSuchAlgorithmException e) {
      logger.error("Error while setting secret key: {}", e.toString());
    } catch (UnsupportedEncodingException e) {
      logger.error("Error while setting secret key: {}", e.toString());
    }
  }

  public static String encrypt(String strToEncrypt) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
      String enc = new String(cipher.doFinal(strToEncrypt.getBytes()));
      return enc;
    } catch (Exception e) {
      logger.error("Error while encrypting: {}", e.toString());
    }
    return null;
  }

  public static String decrypt(String strToDecrypt) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
      return new String(cipher.doFinal(strToDecrypt.getBytes()));
    } catch (Exception e) {
      logger.error("Error while decrypting: {}", e, e);
    }
    return null;
  }

  @PostConstruct
  public void getSecretKey() {
    secret = environment.getRequiredProperty("security.key");
    setKey(secret);
  }
}
