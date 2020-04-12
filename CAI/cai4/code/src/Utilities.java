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

    public Utilities(String method) {
        this.method = method;
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
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            }
            crypted = cipher.doFinal(input);
            return crypted;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public byte[] decrypt(byte[] input, String key) {
        byte[] output = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(this.method);
            if (this.method.equals("AES/GCM/NoPadding")) {
                cipher.init(Cipher.DECRYPT_MODE, skey, gcmParameterSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, skey);
            }
            output = cipher.doFinal(input);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return output;
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    public static String encryptRSA(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decryptRSA(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), "UTF-8");
    }

    public static byte[] encryptCHA(byte[] plaintext, SecretKey key) throws Exception {
        byte[] nonceBytes = new byte[12];

        Cipher cipher = Cipher.getInstance("ChaCha20-Poly1305/None/NoPadding");

        AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(nonceBytes);

        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "ChaCha20");

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

        byte[] cipherText = cipher.doFinal(plaintext);

        return cipherText;
    }

    public static String decryptCHA(byte[] cipherText, SecretKey key) throws Exception {
        byte[] nonceBytes = new byte[12];

        Cipher cipher = Cipher.getInstance("ChaCha20-Poly1305/None/NoPadding");

        AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(nonceBytes);

        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "ChaCha20");

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }


    public static void main(String[] args) throws Exception {
        String key = "mvLBiZsiTbGwrfJB";
        String data = "ABC";
        Auxiliar auxiliar = new Auxiliar();
        byte[] data_bytes = auxiliar.readBytesFromFile("./src/images/image.jpg");
        List<String> methods = Arrays.asList("AES/GCM/NoPadding", "AES/ECB/PKCS5Padding");

        for (String method : methods) {
            String name_method = method.replace("/", "_");
            Utilities utilities = new Utilities(method);
            long start_encrypt = System.currentTimeMillis();
            byte[] encrypt = utilities.encrypt(data_bytes, key);
            long finish_encrypt = System.currentTimeMillis();
            Double timeEncrypt = (finish_encrypt - start_encrypt) / 1000.;
            auxiliar.getImageFromByteArray(encrypt, String.format("image_encrypt_%s.jpg", name_method));

            long start_decrypt = System.currentTimeMillis();
            byte[] decrypt = utilities.decrypt(encrypt, key);
            long finish_decrypt = System.currentTimeMillis();
            Double timeDecrypt = (finish_decrypt - start_decrypt) / 1000.;
            auxiliar.getImageFromByteArray(decrypt, String.format("image_decrypt_%s.jpg", name_method));


            String string_original = new String(data_bytes);
            String string_decrypt = new String(decrypt);

            Boolean equals = string_original.equals(string_decrypt);


            System.out.println("Método utilizado: " + method);
            System.out.println(String.format("Cadena encriptada ha tardando %s en realizarse", timeEncrypt));
            System.out.println(String.format("Cadena desencriptada ha tardando %s en realizarse", timeDecrypt));
            System.out.println(String.format(String.format("¿Original es igual a desencriptado? %s\n", equals)));
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

        long start_encrypt_CHA = System.currentTimeMillis();

        KeyGenerator keyGen = KeyGenerator.getInstance("ChaCha20");
        SecretKey secretKey = keyGen.generateKey();

        byte[] dataArray = data.getBytes();


        byte[] cipherTextCha = encryptCHA(dataArray, secretKey);

        long finish_encrypt_CHA = System.currentTimeMillis();

        Double timeEncryptCHA = (finish_encrypt_CHA - start_encrypt_CHA) / 1000.;


        long start_decrypt_CHA = System.currentTimeMillis();

        String decipherTextCha = decryptCHA(cipherTextCha, secretKey);

        long finish_decrypt_CHA = System.currentTimeMillis();
        Double timeDecryptCHA = (finish_decrypt_CHA - start_decrypt_CHA) / 1000.;

        System.out.println("Método utilizado: ChaCha20-Poly1305");
        System.out.println(String.format("Cadena encriptada: %s tardando %s en realizarse", cipherTextCha, timeEncryptCHA));
        System.out.println(String.format("Cadena desencriptada: %s tardando %s en realizarse\n", decipherTextCha, timeDecryptCHA));


    }

}
