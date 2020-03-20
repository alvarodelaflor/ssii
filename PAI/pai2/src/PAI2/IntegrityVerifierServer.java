package PAI2;

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
import javax.net.ServerSocketFactory;

import java.nio.file.Path;
import java.nio.file.Paths;


public class IntegrityVerifierServer {
	
	 private ServerSocket serverSocket;
	 // Constructor del Servidor
	
	 public IntegrityVerifierServer() throws Exception {
	 
	 // ServerSocketFactory para construir los ServerSockets
	 ServerSocketFactory socketFactory = ( ServerSocketFactory ) ServerSocketFactory.getDefault();
	 
	 // Creaci�n de un objeto ServerSocket escuchando peticiones en el puerto 7070
	 serverSocket = (ServerSocket ) socketFactory.createServerSocket(7070);
	 }
	 
	 // Ejecuci�n del servidor para escuchar peticiones de los clientes
	 private void runServer() throws InvalidKeyException, NoSuchAlgorithmException{
	 while (true) {
	
	// Espera las peticiones del cliente para comprobar mensaje/MAC
	 try {
	 System.err.println( "Esperando conexiones de clientes...");
	 Socket socket = ( Socket ) serverSocket.accept();
	 
	 // Abre un BufferedReader para leer los datos del cliente
	 BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	 
	 // Abre un PrintWriter para enviar datos al cliente
	PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream() ) );
	 
	// Se lee del cliente el mensaje y el macdelMensajeEnviado
	 String mensaje = input.readLine();
	 
	 String nonce = input.readLine();
	 
	 // A continuaci�n habr�a que calcular el mac del MensajeEnviado que podr�a ser
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
	 
	 
	 String macDelMensajeCalculado = bytesToHex(digest);
	 
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
		 output.println( "Mensaje enviado integro " );
		 File fw = new File("src/nonce.log");
		 BufferedWriter bw = new BufferedWriter(new FileWriter(fw, true));
		 bw.append(nonce);
		 bw.newLine();
		 bw.close();
		 
	 } else {
		 output.println( "Mensaje enviado no integro." );
		 
		 File fw = new File("src/logFile.log");
		 BufferedWriter bw = new BufferedWriter(new FileWriter(fw, true));
		 Date date = new Date();
		 bw.append("ERROR: " + date + "\n" + "Integrity message has been failure. Message: " + mensaje);
		 bw.newLine();
		 bw.close();
	 }
	 output.close();
	 input.close();
	 socket.close();
	 }
	 catch ( IOException ioException ) { ioException.printStackTrace(); }
	 }
	 }
	 
	 public static void main( String args[] ) throws Exception
	 {
	 IntegrityVerifierServer server = new IntegrityVerifierServer();
	 server.runServer();
	 }
	 
	 private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	 public static String bytesToHex(byte[] bytes) {
	     char[] hexChars = new char[bytes.length * 2];
	     for (int j = 0; j < bytes.length; j++) {
	         int v = bytes[j] & 0xFF;
	         hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	         hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	     }
	     return new String(hexChars);
	 }

}