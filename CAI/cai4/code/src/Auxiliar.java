import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    public void executeWithoutDelete(List<String> methods) {
        //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////
        Auxiliar auxiliar = new Auxiliar();
        byte[] data_bytes = auxiliar.readBytesFromFile("./src/images/image.jpg");
        String key = "mvLBiZsiTbGwrfJB";
        //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////

        for (String method : methods) {
            String name_method = method.replace("/", "_");
            Utilities utilities = new Utilities(method);
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
                System.out.println(String.format("Cadena encriptada ha tardando %s en realizarse", timeEncrypt));
                System.out.println(String.format("Cadena desencriptada ha tardando %s en realizarse", timeDecrypt));
                System.out.println(String.format(String.format("¿Original es igual a desencriptado? %s\n", result)));
            } else {
                System.err.println(String.format("Unsupported algorithm: %s\n", name_method));
            }
        }
    }

    public void executeWithDelete(String method) {
        //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////
        Auxiliar auxiliar = new Auxiliar();
        byte[] data_bytes = auxiliar.readBytesFromFile("./src/imagesTest/image.jpg");
        String key = "mvLBiZsiTbGwrfJB";
        //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////

        String name_method = method.replace("/", "_");
        Utilities utilities = new Utilities(method);
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
//            auxiliar.getImageFromByteArray(decrypt, String.format("image_decrypt_%s.jpg", name_method));

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

    public void executeDecrypt(String method) {
        //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////
        Auxiliar auxiliar = new Auxiliar();
        byte[] data_bytes = auxiliar.readBytesFromFile("./src/imagesTest/image.jpg");
        String key = "mvLBiZsiTbGwrfJB";
        //////////////////////////////////////////////////////////// MAIN CLASS////////////////////////////////////////////////////////////

        String name_method = method.replace("/", "_");
        Utilities utilities = new Utilities(method);

        long start_decrypt = System.currentTimeMillis();
        byte[] decrypt = utilities.decrypt(data_bytes, key);
        long finish_decrypt = System.currentTimeMillis();
        Double timeDecrypt = (finish_decrypt - start_decrypt) / 1000.;
        auxiliar.getImageFromByteArray(decrypt, String.format("image.jpg", name_method), true);

        String string_original = new String(data_bytes);
        String string_decrypt = new String(decrypt);
        Boolean equals = string_original.equals(string_decrypt);
        String result = equals.toString().replaceAll("true", "Sí").replaceAll("false", "No");

        System.out.println("Método utilizado: " + method);
        System.out.println(String.format("Cadena desencriptada ha tardando %s en realizarse", timeDecrypt));
        System.out.println(String.format(String.format("¿Original es igual a desencriptado? %s\n", result)));

    }
}
