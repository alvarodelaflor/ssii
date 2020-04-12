package code;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

public class Utilities {

    private String method;
    public final int AES_KEY_SIZE = 256;
    public final int GCM_IV_LENGTH = 12;
    public final int GCM_TAG_LENGTH = 16;
    byte[] IV = new byte[GCM_IV_LENGTH];
    public GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);
    public String key;
    public SecretKey secretKey;

    public Utilities(String method, String key) {
        this.method = method;
        this.secretKey = generateSecretKey();
        this.key = key;
    }

    public SecretKey generateSecretKey() {
        SecretKey secretKey = null;
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get("./src/key/key"));
            if (keyBytes.length <= 0) {
                KeyGenerator keyGen = KeyGenerator.getInstance("ChaCha20");
                secretKey = keyGen.generateKey();
                byte[] encoded = secretKey.getEncoded();
                Files.write(Paths.get("./src/key/key"), encoded);
            } else {
                secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "ChaCha20");
            }
        } catch (Exception e) {
            System.out.println("An exception occurs while generating a secret key" + e);
        }
        return secretKey;
    }

    public static String encryptRSA(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public byte[] encrypt(byte[] input, String key) {
        byte[] crypted = null;
        Cipher cipher = null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            cipher = Cipher.getInstance(this.method);
            if (this.method.equals("AES/GCM/NoPadding")) {
                GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
            } else if (this.method.equals("AES/ECB/PKCS5Padding")) {
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            } else if (this.method.equals("ChaCha20-Poly1305/None/NoPadding")) {
                byte[] nonceBytes = new byte[12];
                AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(nonceBytes);
                SecretKeySpec keySpec2 = new SecretKeySpec(this.secretKey.getEncoded(), "ChaCha20");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec2, ivParameterSpec);
            }
            crypted = cipher.doFinal(input);
        } catch (Exception e) {
            crypted = null;
        }
        return crypted;
    }

    public static String decryptRSA(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), "UTF-8");
    }

    public byte[] decrypt(byte[] input, String key) {
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(this.method);
            if (this.method.equals("AES/GCM/NoPadding")) {
                cipher.init(Cipher.DECRYPT_MODE, skey, gcmParameterSpec);
            } else if (this.method.equals("AES/ECB/PKCS5Padding")) {
                cipher.init(Cipher.DECRYPT_MODE, skey);
            } else if (this.method.equals("ChaCha20-Poly1305/None/NoPadding")) {
                byte[] nonceBytes = new byte[12];
                AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(nonceBytes);
                SecretKeySpec keySpec = new SecretKeySpec(this.secretKey.getEncoded(), "ChaCha20");
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            }
            output = cipher.doFinal(input);
        } catch (Exception e) {
            output = null;
        }
        return output;
    }
}
