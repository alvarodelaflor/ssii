package Test6;

import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.SocketFactory;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import Test1.Nonce;


public class IntegrityVerifierClient {

	public Utilities utilities = new Utilities();

    // Constructor que abre una conexión Socket para enviar mensaje/MAC al servidor
    public IntegrityVerifierClient() throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
            Socket socket = (Socket) socketFactory.createSocket("localhost", 7070);

            // Crea un PrintWriter para enviar mensaje/MAC al servidor
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String mensaje = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");

            // Envío del mensaje al servidor
            output.println(mensaje);

            // Clave compartida client - server
            String secreto = "secreto";

            // Construcción del Nonce
            String nonce = Nonce.createRandomNonce();
            output.println(nonce);

            // generate a key from the generator
            SecretKeySpec key = new SecretKeySpec(secreto.getBytes(), "HmacSHA256");

            // Construcción de la MAC
            final Mac mac_SHA256 = Mac.getInstance("HmacSHA256");
            mac_SHA256.init(key);

            String mensajeNonce = mensaje + nonce;

            // get the string as UTF-8 bytes
            byte[] b = mensajeNonce.getBytes("UTF-8");

            // create a digest from the byte array
            byte[] digest = mac_SHA256.doFinal(b);

            String digestHex = utilities.bytesToHex(digest);

            // Habría que calcular el correspondiente MAC con la clave compartida por servidor/cliente
            output.println(digestHex);

            // Importante para que el mensaje se envíe
            output.flush();

//----------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------
            // Crea un objeto BufferedReader para leer la respuesta del servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Se lee del servidor el mensaje y el macdelMensajeEnviado
            String mensajeServer = "Su mensaje no es integro";

            String nonceServer = input.readLine();

            // A continuación habría que calcular el mac del MensajeEnviado que podría ser
            String macServer = input.readLine();

            //mac del MensajeCalculado

            final Mac mac_SHA256_server = Mac.getInstance("HmacSHA256");

            SecretKeySpec key_verify = new SecretKeySpec(secreto.getBytes(), "HmacSHA256");

            mac_SHA256_server.init(key_verify);

            String mensajeNonceServer = mensajeServer + nonceServer;

            // get the string as UTF-8 bytes
            byte[] bServer = mensajeNonceServer.getBytes("UTF-8");

            // create a digest from the byte array
            byte[] digest_server = mac_SHA256_server.doFinal(bServer);


            String macDelMensajeCalculado = utilities.bytesToHex(digest_server);

            FileReader linesNonce = new FileReader("src/nonce_client.log");

            Boolean nonceValid = true;

            try (BufferedReader br = new BufferedReader(linesNonce)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.equals(nonceServer)) {
                        nonceValid = false;
                    }
                }
            }

            if (macServer.equals(macDelMensajeCalculado) && nonceValid) {
                JOptionPane.showMessageDialog(null, mensajeServer);
                
                File fw = new File("src/nonce_client.log");
                BufferedWriter bw = new BufferedWriter(new FileWriter(fw, true));
                bw.append(nonce);
                bw.newLine();
                bw.close();
                
            } else {
                JOptionPane.showMessageDialog(null, "La respuesta del servidor no ha sido integra");

                File fw = new File("src/logFile_client.log");
                BufferedWriter bw = new BufferedWriter(new FileWriter(fw, true));
                Date date = new Date();
                bw.append("ERROR: " + date + "\n" + "Integrity message has been failure. Message: " + mensaje);
                bw.newLine();
                bw.close();
            }

            // Se cierra la conexion
            output.close();
            input.close();
            socket.close();
        }
        // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

        // Salida de la aplicacion
        finally {
            System.exit(0);
        }
    }

    public static void main(String args[]) throws InvalidKeyException, NoSuchAlgorithmException {
        new IntegrityVerifierClient();
    }

}