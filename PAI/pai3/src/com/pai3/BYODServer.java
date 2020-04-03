package com.pai3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class BYODServer {

    private ServerSocket serverSocket;
	public Utilities utilities = new Utilities();

	// Constructor del Servidor
    public BYODServer() throws Exception {

        // espera conexiones del cliente y comprueba login
        SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        // crea Socket de la factoría
        SSLServerSocket serverSocket = (SSLServerSocket) socketFactory.createServerSocket(7070);
    }

    // Ejecución del servidor para escuchar peticiones de los clientes
    private void runServer() throws InvalidKeyException, NoSuchAlgorithmException {
        while (true) {

            // Espera las peticiones del cliente para comprobar mensaje/MAC
            try {
                System.err.println("Esperando conexiones de clientes...");
                Socket socket = (Socket) serverSocket.accept();

                // Abre un BufferedReader para leer los datos del cliente
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Abre un PrintWriter para enviar datos al cliente
                PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                // Se lee del cliente el mensaje y el macdelMensajeEnviado
                String mensaje = input.readLine();

                String nonce = input.readLine();

                // A continuación habría que calcular el mac del MensajeEnviado que podría ser
                String macdelMensajeEnviado = input.readLine();

                //mac del MensajeCalculado

                String secreto = "secreto";

                final Mac mac_SHA256 = Mac.getInstance("HmacSHA256");

                SecretKeySpec key = new SecretKeySpec(secreto.getBytes(), "HmacSHA256");

                mac_SHA256.init(key);

                String mensajeNonce = mensaje + nonce;

                // get the string as UTF-8 bytes
                byte[] b = mensajeNonce.getBytes("UTF-8");

                // create a digest from the byte array
                byte[] digest = mac_SHA256.doFinal(b);


                String macDelMensajeCalculado = utilities.bytesToHex(digest);

                FileReader linesNonce = new FileReader("src/nonce.log");

                Boolean nonceValid = true;

                try (BufferedReader br = new BufferedReader(linesNonce)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.equals(nonce)) {
                            nonceValid = false;
                        }
                    }
                }
                if (macdelMensajeEnviado.equals(macDelMensajeCalculado) && nonceValid) {
                    output.println("Mensaje enviado integro");
                    File fw = new File("src/nonce.log");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(fw, true));
                    bw.append(nonce);
                    bw.newLine();
                    bw.close();
                    // Construcción del Nonce
                    String nonce_pass = Nonce.createRandomNonce();
                    output.println(nonce_pass);

                    // generate a key from the generator
                    SecretKeySpec key_pass = new SecretKeySpec(secreto.getBytes(), "HmacSHA256");

                    // Construcción de la MAC
                    final Mac mac_SHA256_pass = Mac.getInstance("HmacSHA256");
                    mac_SHA256_pass.init(key_pass);

                    String mensajeNonce_pass = "Mensaje enviado integro" + nonce_pass;

                    // get the string as UTF-8 bytes
                    byte[] b_pass = mensajeNonce_pass.getBytes("UTF-8");

                    // create a digest from the byte array
                    byte[] digest_pass = mac_SHA256_pass.doFinal(b_pass);

                    String digestHex_pass = utilities.bytesToHex(digest_pass);

                    // Habría que calcular el correspondiente MAC con la clave compartida por servidor/cliente
                    output.println(digestHex_pass);

                    // Importante para que el mensaje se envíe
                    output.flush();

                } else {
                    output.println("Mensaje enviado no integro.");

                    File fw = new File("src/logFile.log");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(fw, true));
                    Date date = new Date();
                    bw.append("ERROR: " + date + "\n" + "Integrity message has been failure. Message: " + mensaje);
                    bw.newLine();
                    bw.close();

                    // Construcción del Nonce
                    String nonce_pass = Nonce.createRandomNonce();
                    output.println(nonce_pass);

                    // generate a key from the generator
                    SecretKeySpec key_pass = new SecretKeySpec(secreto.getBytes(), "HmacSHA256");

                    // Construcción de la MAC
                    final Mac mac_SHA256_pass = Mac.getInstance("HmacSHA256");
                    mac_SHA256_pass.init(key_pass);

                    String mensajeNonce_pass = "Mensaje enviado no integro." + nonce_pass;

                    // get the string as UTF-8 bytes
                    byte[] b_pass = mensajeNonce_pass.getBytes("UTF-8");

                    // create a digest from the byte array
                    byte[] digest_pass = mac_SHA256_pass.doFinal(b_pass);

                    String digestHex_pass = utilities.bytesToHex(digest_pass);

                    // Habría que calcular el correspondiente MAC con la clave compartida por servidor/cliente
                    output.println(digestHex_pass);

                    // Importante para que el mensaje se envíe
                    output.flush();
                }
                output.close();
                input.close();
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws Exception {
        BYODServer server = new BYODServer();
        server.runServer();
    }
}
