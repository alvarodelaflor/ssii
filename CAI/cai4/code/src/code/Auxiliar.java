package code;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.security.Key;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Auxiliar {

    public static Boolean getImageFromByteArray(byte[] image_bytes, String pathToSave, Boolean test) {
        try {
            FileOutputStream stream = null;
            if (test) {
                stream = new FileOutputStream("./src/imagesTest/" + pathToSave);
            } else {
                stream = new FileOutputStream("./src/images/" + pathToSave);
            }
            stream.write(image_bytes);
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }

    public void executeWithoutDelete(List<String> methods, String key) {
        //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////
        Auxiliar auxiliar = new Auxiliar();
        byte[] data_bytes = auxiliar.readBytesFromFile("./src/images/image.jpg");
//        String key1 = "mvLBiZsiTbGwrfJB";
        //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////

        for (String method : methods) {
            String name_method = method.replace("/", "_");
            Utilities utilities = new Utilities(method, key);
            long start_encrypt = System.currentTimeMillis();
            byte[] encrypt = utilities.encrypt(data_bytes, key);
            long finish_encrypt = System.currentTimeMillis();
            Double timeEncrypt = (finish_encrypt - start_encrypt) / 1000.;

            if (encrypt != null) {
                auxiliar.getImageFromByteArray(encrypt, String.format("image_encrypt_%s.jpg", name_method), false);

                long start_decrypt = System.currentTimeMillis();
                byte[] decrypt = utilities.decrypt(encrypt, key);
                long finish_decrypt = System.currentTimeMillis();
                Double timeDecrypt = (finish_decrypt - start_decrypt) / 1000.;
                auxiliar.getImageFromByteArray(decrypt, String.format("image_decrypt_%s.jpg", name_method), false);

                String string_original = new String(data_bytes);
                String string_decrypt = new String(decrypt);
                Boolean equals = string_original.equals(string_decrypt);
                String result = equals.toString().replaceAll("true", "Sí").replaceAll("false", "No");

                System.out.println("Método utilizado: " + method);
                System.out.println(String.format("Archivo encriptado, ha tardando %s en realizarse", timeEncrypt));
                System.out.println(String.format("Archivo desencriptado, ha tardando %s en realizarse", timeDecrypt));
                System.out.println(String.format(String.format("¿Original es igual a desencriptado? %s\n", result)));
            } else {
                System.err.println(String.format("Unsupported algorithm: %s\n", name_method));
            }
        }
    }

    public void executeWithDelete(String method, String key) {
        try {
            //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////
            Auxiliar auxiliar = new Auxiliar();
            byte[] data_bytes = auxiliar.readBytesFromFile("./src/imagesTest/image.jpg");
//            String key1 = "mvLBiZsiTbGwrfJB";
            //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////
            String name_method = method.replace("/", "_");
            Utilities utilities = new Utilities(method, key);
            long start_encrypt = System.currentTimeMillis();
            byte[] encrypt = utilities.encrypt(data_bytes, key);
            long finish_encrypt = System.currentTimeMillis();
            Double timeEncrypt = (finish_encrypt - start_encrypt) / 1000.;

            if (encrypt != null) {
                auxiliar.getImageFromByteArray(encrypt, String.format("image.jpg", name_method), true);

                long start_decrypt = System.currentTimeMillis();
                byte[] decrypt = utilities.decrypt(encrypt, key);
                long finish_decrypt = System.currentTimeMillis();
                Double timeDecrypt = (finish_decrypt - start_decrypt) / 1000.;

                String string_original = new String(data_bytes);
                String string_decrypt = new String(decrypt);
                Boolean equals = string_original.equals(string_decrypt);
                String result = equals.toString().replaceAll("true", "Sí").replaceAll("false", "No");

                System.out.println("Método utilizado: " + method);
                System.out.println(String.format("Archivo encriptado, ha tardando %s en realizarse", timeEncrypt));
                System.out.println(String.format("Archivo desencriptado, ha tardando %s en realizarse", timeDecrypt));
                System.out.println(String.format(String.format("¿Original es igual a desencriptado? %s\n", result)));
                
                if (method.equals("ChaCha20-Poly1305/None/NoPadding")) {
                	char[] pwdArray = "password".toCharArray();
            		
            		System.out.println(pwdArray);

            		KeyStore ks = KeyStore.getInstance("JKS");
            		
            		ks.load(new FileInputStream("src/keystoreCha.jks"), pwdArray);
            		
            		System.out.println(ks);
            		
            		byte[] decodedKey = Base64.getDecoder().decode(key);
            		// rebuild key using SecretKeySpec
            		SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
            		
            		System.out.println(originalKey);
            		
            		
            		KeyStore.SecretKeyEntry secret
            		 = new KeyStore.SecretKeyEntry(originalKey);
            		KeyStore.ProtectionParameter password
            		 = new KeyStore.PasswordProtection(pwdArray);
            		if (ks.containsAlias("db-encryption-secret-image")) {
            			ks.deleteEntry("db-encryption-secret-image");
            		}
            		ks.setEntry("db-encryption-secret-image", secret, password);

            		ks.store(new FileOutputStream("src/keystoreCha.jks"), pwdArray);
                }
            } else {
                System.err.println(String.format("Unsupported algorithm: %s\n", name_method));
            }
        } catch (Exception e) {
            System.out.println("Se ha producido un error al encriptar");
        }
    }

    public void executeDecrypt(String method, String key) {
        try {
            //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////
            Auxiliar auxiliar = new Auxiliar();
            byte[] data_bytes = auxiliar.readBytesFromFile("./src/imagesTest/image.jpg");
//            String key1 = "mvLBiZsiTbGwrfJB";
            //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////

            String name_method = method.replace("/", "_");
            Utilities utilities = new Utilities(method, key);
            
            if (method.equals("ChaCha20-Poly1305/None/NoPadding")) {
            	char[] pwdArray = "password".toCharArray();
        		
        		System.out.println(pwdArray);

        		KeyStore ks = KeyStore.getInstance("JKS");
        		
        		ks.load(new FileInputStream("src/keystoreCha.jks"), pwdArray);
        		
        		Key ssoSigningKey = ks.getKey("db-encryption-secret-image", pwdArray);
        		
        		byte[] decodedKey = Base64.getDecoder().decode(key);
        		// rebuild key using SecretKeySpec
        		SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
        		
        		if(ssoSigningKey.equals(originalKey)) {
        			long start_decrypt = System.currentTimeMillis();
                    byte[] decrypt = utilities.decrypt(data_bytes, key);
                    long finish_decrypt = System.currentTimeMillis();
                    Double timeDecrypt = (finish_decrypt - start_decrypt) / 1000.;
                    auxiliar.getImageFromByteArray(decrypt, String.format("image.jpg", name_method), true);


                    System.out.println("Método utilizado: " + method);
                    System.out.println(String.format("Archivo desencriptado, ha tardando %s en realizarse", timeDecrypt));
        		}else {
            		System.out.println("clave incorrecta");

        		}  	
            }else {

            long start_decrypt = System.currentTimeMillis();
            byte[] decrypt = utilities.decrypt(data_bytes, key);
            long finish_decrypt = System.currentTimeMillis();
            Double timeDecrypt = (finish_decrypt - start_decrypt) / 1000.;
            auxiliar.getImageFromByteArray(decrypt, String.format("image.jpg", name_method), true);


            System.out.println("Método utilizado: " + method);
            System.out.println(String.format("Archivo desencriptado, ha tardando %s en realizarse", timeDecrypt));
            }
        } catch (Exception e) {
            System.out.println("Se ha producido un error al desencriptar");
        }
    }

    public void initProgram() {
        try {

            System.out.println("Introduzca la clave secreta que desee para cifrar y descifrar el archivo\nDebe ser una cadena de 16 caracteres, por ejemplo, mvLBiZsiTbGwrfJB\nClave (q para salir): ");
            String key = "";
            InputStreamReader isr1;
            BufferedReader br1;
            while (key.length() < 16 || key.length() > 16) {
                isr1 = new InputStreamReader(System.in);
                br1 = new BufferedReader(isr1);
                key = br1.readLine();
                if (key.equals("q")) {
                    System.exit(1);
                }
                if (key.length() < 16 || key.length() > 16) {
                    System.out.println("La clave tiene que contener 16 caracteres exactos.\nSi desea cancelar la ejecución del programa pulse q\nClave: ");
                }
            }

            System.out.println("Seleccine el método que desee entre los distintos casos:\n" +
                    "0: Se ejecutarán todos los cifrados, utilizando la imagen 'image.jpg' de la carpeta ./src/images donde se mostrarán todas las imagenes cifradas y su resultado tras descifrar\n" +
                    "1: Se cifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'AES/GCM/NoPadding'\n" +
                    "2: Se cifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'AES/ECB/PKCS5Padding'\n" +
                    "3: Se cifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'ChaCha20-Poly1305/None/NoPadding'\n" +
                    "4: Se cifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'DES/ECB/PKCS5PADDING'\n" +
                    "5: Se descifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'AES/GCM/NoPadding'\n" +
                    "6: Se descifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'AES/ECB/PKCS5Padding'\n" +
                    "7: Se descifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'ChaCha20-Poly1305/None/NoPadding'\n" +
                    "8: Se descifrará la imagen 'image.jpg' de la carpeta ./src/imagesTest/ borrando la original usuando 'DES/ECB/PKCS5PADDING'\n" +
                    "Escriba su elección (q para salir): ");

            String cadena = "";
            Boolean res = true;
            Integer selected = null;
            List<Integer> validOptions = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
            InputStreamReader isr2 = new InputStreamReader(System.in);
            BufferedReader br2 = new BufferedReader(isr2);
            while (res) {
                isr2 = new InputStreamReader(System.in);
                br2 = new BufferedReader(isr2);
                cadena = br2.readLine();
                try {
                    if (cadena.equals("q")) {
                        System.exit(1);
                    }
                    selected = Integer.parseInt(cadena);
                } catch (Exception e) {
                    selected = -1;
                }
                res = !validOptions.contains(selected);
                if (res) {
                    System.out.println("Introduzca una opción válida, puede ser: " + validOptions.toString() + "\nElección: ");
                }
            }

            List<String> methods = null;
            switch (selected) {
                case 0:
                    methods = Arrays.asList("AES/GCM/NoPadding", "AES/ECB/PKCS5Padding", "ChaCha20-Poly1305/None/NoPadding", "DES/ECB/PKCS5PADDING");
                    new Auxiliar().executeWithoutDelete(methods, key);
                    break;
                case 1:
                    new Auxiliar().executeWithDelete("AES/GCM/NoPadding", key);
                    break;
                case 2:
                    new Auxiliar().executeWithDelete("AES/ECB/PKCS5Padding", key);
                    break;
                case 3:
                    new Auxiliar().executeWithDelete("ChaCha20-Poly1305/None/NoPadding", key);
                    break;
                case 4:
                    new Auxiliar().executeWithDelete("DES/ECB/PKCS5PADDING", key);
                    break;
                case 5:
                    new Auxiliar().executeDecrypt("AES/GCM/NoPadding", key);
                    break;
                case 6:
                    new Auxiliar().executeDecrypt("AES/ECB/PKCS5Padding", key);
                    break;
                case 7:
                    new Auxiliar().executeDecrypt("ChaCha20-Poly1305/None/NoPadding", key);
                    break;
                case 8:
                    new Auxiliar().executeDecrypt("DES/ECB/PKCS5PADDING", key);
                    break;
            }

        } catch (Exception e) {
            System.out.println("Se ha producido un error. No se puede ejecutar el programa");
        }
    }

    public void doTest() {
        try {
            Integer i = 0;
            Integer photo = 2;
            while (i < 4) {
                File file = new File("./src/images/image.jpg");
                file.delete();
                File source = new File(String.format("./src/clusterImages/%sk_kb.jpg", photo));
                photo += 2;
                File dest = new File("./src/images/image.jpg");
                FileUtils.copyFile(source, dest);
                System.out.println(String.format("Test number %s", i));
                List<String> methods = Arrays.asList("AES/GCM/NoPadding", "AES/ECB/PKCS5Padding", "ChaCha20-Poly1305/None/NoPadding", "DES/ECB/PKCS5PADDING");
                new Auxiliar().executeWithoutDelete(methods, "mvLBiZsiTbGwrfJB");
                i++;
            }
            File file = new File("./src/images/image.jpg");
            file.delete();
            File source = new File(String.format("./src/clusterImages/image.jpg", 2));
            File dest = new File("./src/images/image.jpg");
            FileUtils.copyFile(source, dest);
        } catch (Exception e) {
            System.out.println("Se ha producido un error ejecutando el test " + e);
        }
    }
}
