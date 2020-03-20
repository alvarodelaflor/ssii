package PAI2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.SocketFactory;
import javax.swing.JOptionPane;


public class IntegrityVerifierClient {
	
	// Constructor que abre una conexión Socket para enviar mensaje/MAC al servidor
	 public IntegrityVerifierClient() throws NoSuchAlgorithmException, InvalidKeyException{
	 try {
	 SocketFactory socketFactory = ( SocketFactory ) SocketFactory.getDefault();
	 Socket socket = ( Socket ) socketFactory.createSocket("localhost", 7070 );
	
	 // Crea un PrintWriter para enviar mensaje/MAC al servidor
	 PrintWriter output = new PrintWriter(new OutputStreamWriter( socket.getOutputStream() ) );
	 String mensaje = JOptionPane.showInputDialog(null,"Introduzca su mensaje:" );
	 
	 // Envío del mensaje al servidor
	 output.println(mensaje);
	 
	 String secreto = "secreto";
	 
	 final Mac mac_SHA256 = Mac.getInstance("HmacSHA256");
	 
	// generate a key from the generator
     SecretKeySpec key = new SecretKeySpec(secreto.getBytes(), "HmacSHA256");

     mac_SHA256.init(key);

     // get the string as UTF-8 bytes
     byte[] b = mensaje.getBytes("UTF-8");

     // create a digest from the byte array
     byte[] digest = mac_SHA256.doFinal(b);
     
     String digestHex = bytesToHex(digest);
          	 
	 // Habría que calcular el correspondiente MAC con la clave compartida por servidor/cliente
	 output.println(digestHex);
	 
	 // Importante para que el mensaje se envíe
	 output.flush();
	 
	 
	 // Crea un objeto BufferedReader para leer la respuesta del servidor
	 BufferedReader input = new BufferedReader(new InputStreamReader( socket.getInputStream ()) );
	 
	 // Lee la respuesta del servidor
	 String respuesta = input.readLine();
	
	 // Muestra la respuesta al cliente
	 JOptionPane.showMessageDialog( null, respuesta);
	 
	 // Se cierra la conexion
	 output.close();
	 input.close();
	 socket.close();
	 } 
	 // end try
	 catch ( IOException ioException ) {
	 ioException.printStackTrace();
	 }
	
	 // Salida de la aplicacion
	 finally {
	 System.exit( 0 );
	 }
	 }
	 
	 public static void main( String args[]) throws InvalidKeyException, NoSuchAlgorithmException{
		 new IntegrityVerifierClient();
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