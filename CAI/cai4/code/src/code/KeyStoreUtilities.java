package code;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

public class KeyStoreUtilities {

    public String keystoreLocation = "./src/key/clientkeystore";
    public String keyStorePassword = "talaveron";

    public String getPasswordFromKeystore(byte[] entry) {
        try {
            KeyStore ks = KeyStore.getInstance("JCEKS");
            ks.load(null, keyStorePassword.toCharArray());
            KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());

            FileInputStream fIn = new FileInputStream(keystoreLocation);

            ks.load(fIn, keyStorePassword.toCharArray());

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");

            KeyStore.SecretKeyEntry ske = (KeyStore.SecretKeyEntry) ks.getEntry(new String(entry).substring(0, 10000), keyStorePP);

            PBEKeySpec keySpec = (PBEKeySpec) factory.getKeySpec(ske.getSecretKey(), PBEKeySpec.class);

            char[] password = keySpec.getPassword();

            return new String(password);
        } catch (Exception e) {
            System.err.println("An error occur when the encript key was save");
            return null;
        }
    }

    public void makeNewKeystoreEntry(byte[] entry, String entryPassword)
            throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBE");
        SecretKey generatedSecret = factory.generateSecret(new PBEKeySpec(entryPassword.toCharArray()));

        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(null, keyStorePassword.toCharArray());
        KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());

        ks.setEntry(new String(entry).substring(0, 10000), new KeyStore.SecretKeyEntry(generatedSecret), keyStorePP);

        FileOutputStream fos = new java.io.FileOutputStream(keystoreLocation);
        ks.store(fos, keyStorePassword.toCharArray());
    }
}
