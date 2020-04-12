import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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
        String key = "mvLBiZsiTbGwrfJB";
        String data = "ABC";

        //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////
        Auxiliar auxiliar = new Auxiliar();
        byte[] data_bytes = auxiliar.readBytesFromFile("./src/images/image.jpg");
        List<String> methods = Arrays.asList("AES/GCM/NoPadding", "AES/ECB/PKCS5Padding", "ChaCha20-Poly1305/None/NoPadding", "RSA");
        //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////

        for (String method : methods) {
            String name_method = method.replace("/", "_");
            Utilities utilities = new Utilities(method);
            long start_encrypt = System.currentTimeMillis();
            byte[] encrypt = utilities.encrypt(data_bytes, key);
            long finish_encrypt = System.currentTimeMillis();
            Double timeEncrypt = (finish_encrypt - start_encrypt) / 1000.;

            if (encrypt != null) {
                auxiliar.getImageFromByteArray(encrypt, String.format("image_encrypt_%s.jpg", name_method));

                long start_decrypt = System.currentTimeMillis();
                byte[] decrypt = utilities.decrypt(encrypt, key);
                long finish_decrypt = System.currentTimeMillis();
                Double timeDecrypt = (finish_decrypt - start_decrypt) / 1000.;
                auxiliar.getImageFromByteArray(decrypt, String.format("image_decrypt_%s.jpg", name_method));

                String string_original = new String(data_bytes);
                String string_decrypt = new String(decrypt);
                Boolean equals = string_original.equals(string_decrypt);
                String result = equals.toString().replaceAll("true", "Sí").replaceAll("false", "No");

                System.out.println("Método utilizado: " + method);
                System.out.println(String.format("Cadena encriptada ha tardando %s en realizarse", timeEncrypt));
                System.out.println(String.format("Cadena desencriptada ha tardando %s en realizarse", timeDecrypt));
                System.out.println(String.format(String.format("¿Original es igual a desencriptado? %s\n", result)));
            } else {
                System.err.println(String.format("Unsupported algorithm: %s\n", name_method));
            }
        }

        long start_encrypt_RSA = System.currentTimeMillis();

        KeyPair pair = generateKeyPair();

        String cipherText = encryptRSA(data, pair.getPublic());

        long finish_encrypt_RSA = System.currentTimeMillis();

        Double timeEncryptRSA = (finish_encrypt_RSA - start_encrypt_RSA) / 1000.;

        long start_decrypt_RSA = System.currentTimeMillis();

        String decipheredMessage = decryptRSA(cipherText, pair.getPrivate());

        long finish_decrypt_RSA = System.currentTimeMillis();
        Double timeDecryptRSA = (finish_decrypt_RSA - start_decrypt_RSA) / 1000.;

        System.out.println("Método utilizado: RSA-2046");
        System.out.println(String.format("Cadena encriptada: %s tardando %s en realizarse", cipherText, timeEncryptRSA));
        System.out.println(String.format("Cadena desencriptada: %s tardando %s en realizarse\n", decipheredMessage, timeDecryptRSA));


    }

}
