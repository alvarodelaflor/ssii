import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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

    public String encrypt(String input, String key) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(this.method);
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Base64.Encoder encoder = java.util.Base64.getEncoder();

        return new String(encoder.encodeToString(crypted));
    }

    public String decrypt(String input, String key) {
        byte[] output = null;
        try {
            Base64.Decoder decoder = java.util.Base64.getDecoder();
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(this.method);
            if (this.method.equals("AES/GCM/NoPadding")) {
                cipher.init(Cipher.DECRYPT_MODE, skey, gcmParameterSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, skey);
            }
            output = cipher.doFinal(decoder.decode(input));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new String(output);
    }

    public static void main(String[] args) {
        String key = "mvLBiZsiTbGwrfJB";
        String data = "ABC";
        List<String> methods = Arrays.asList("AES/ECB/PKCS5Padding", "AES/GCM/NoPadding");

        for (String method: methods) {
            Utilities utilities = new Utilities(method);
            long start_encrypt = System.currentTimeMillis();
            String encrypt = utilities.encrypt(data, key);
            long finish_encrypt = System.currentTimeMillis();
            Double timeEncrypt = (finish_encrypt - start_encrypt) / 1000.;

            long start_decrypt = System.currentTimeMillis();
            String decrypt = utilities.decrypt(encrypt, key);
            long finish_decrypt = System.currentTimeMillis();
            Double timeDecrypt = (finish_decrypt - start_decrypt) / 1000.;

            System.err.println("Método utilizado: " + method);
            System.out.println(String.format("Cadena encriptada: %s tardando %s en realizarse", encrypt, timeEncrypt));
            System.out.println(String.format("Cadena desencriptada: %s tardando %s en realizarse\n", decrypt, timeDecrypt));
        }

    }

}
