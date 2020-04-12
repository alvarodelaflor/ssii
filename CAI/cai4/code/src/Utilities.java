import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class Utilities {

    private String method;
    public final int AES_KEY_SIZE = 256;
    public final int GCM_IV_LENGTH = 12;
    public final int GCM_TAG_LENGTH = 16;
    byte[] IV = new byte[GCM_IV_LENGTH];
    public GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);
    public KeyPair pair;
    public SecretKey secretKey;

    public Utilities(String method) {
        this.method = method;
        this.pair = generateKeyPairAux();
        this.secretKey = generateSecretKey();
    }

    public SecretKey generateSecretKey() {
        SecretKey secretKey = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("ChaCha20");
            secretKey = keyGen.generateKey();
        } catch (Exception e) {
            System.out.println("An exception occurs while generating a secret key");
        }
        return secretKey;
    }

    public KeyPair generateKeyPairAux() {
        KeyPair pair = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048, new SecureRandom());
            pair = generator.generateKeyPair();
        } catch (Exception e) {
            System.out.println("Exception in generation of KeyPair");
        }
        return pair;
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
            } else if (this.method.equals("RSA")) {
                cipher.init(Cipher.ENCRYPT_MODE, this.pair.getPublic());
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
            } else if (this.method.equals("RSA")) {
                cipher.init(Cipher.DECRYPT_MODE, this.pair.getPrivate());
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

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    public static void main(String[] args) throws Exception {

        System.out.println("Seleccine el método que desee entre los distintos casos:\n" +
                "0: Se ejecutarán todos los cifrados, utilizando la imagen 'image.jpg' de la carpeta ./src/images donde se mostrarán todas las imagenes cifradas y su resultado tras descifrar\n" +
                "1: Se cifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'AES/GCM/NoPadding'\n" +
                "2: Se cifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'AES/ECB/PKCS5Padding'\n" +
                "3: Se cifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'ChaCha20-Poly1305/None/NoPadding'\n" +
                "4: Se descifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'AES/GCM/NoPadding'\n" +
                "5: Se descifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'AES/ECB/PKCS5Padding'\n" +
                "6: Se descifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'ChaCha20-Poly1305/None/NoPadding'\n" +
                "Escriba su elección: ");

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader (isr);
        String cadena = br.readLine();

        int selected = Integer.parseInt(cadena);
        List<String> methods = null;
        switch (selected) {
            case 0:
                methods = Arrays.asList("AES/GCM/NoPadding", "AES/ECB/PKCS5Padding", "ChaCha20-Poly1305/None/NoPadding", "RSA");
                new Auxiliar().executeWithoutDelete(methods);
                break;
            case 1:
                new Auxiliar().executeWithDelete("AES/GCM/NoPadding");
                break;
            case 2:
                new Auxiliar().executeWithDelete("AES/ECB/PKCS5Padding");
                break;
            case 3:
                new Auxiliar().executeWithDelete("ChaCha20-Poly1305/None/NoPadding");
                break;
            case 4:
                new Auxiliar().executeDecrypt("AES/GCM/NoPadding");
                break;
            case 5:
                new Auxiliar().executeDecrypt("AES/ECB/PKCS5Padding");
                break;
            case 6:
                new Auxiliar().executeDecrypt("ChaCha20-Poly1305/None/NoPadding");
                break;
        }
    }

}
