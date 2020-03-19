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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.SocketFactory;
import javax.swing.JOptionPane;

public class IntegrityVerifierClient {
    public IntegrityVerifierClient(){
        try {
            SocketFactory socketFactory = ( SocketFactory ) SocketFactory.getDefault();
            Socket socket = (Socket) socketFactory.createSocket("localhost", 7070 );
            PrintWriter output = new PrintWriter(new OutputStreamWriter( socket.getOutputStream() ) );
            String mensaje = JOptionPane.showInputDialog(null,"Introduzca su mensaje:" );

            output.println(mensaje);
            output.println(this.getMac(mensaje, "test"));

            String macdelMensaje = "";
            output.println(macdelMensaje);
            output.flush();
            BufferedReader input = new BufferedReader(new InputStreamReader( socket.getInputStream ()) );
            String respuesta = input.readLine();
            JOptionPane.showMessageDialog( null, respuesta);
            output.close();
            input.close();
            socket.close();
        } 
        catch ( IOException ioException ) {
            ioException.printStackTrace();
        }
        finally {
            System.exit( 0 );
        }
    }
    // ejecución del cliente de verificación de la integridad
    public static void main( String args[]){
        new IntegrityVerifierClient();
    }
private String getMac(String mensaje, String secreto) {
        try {

            // get a key generator for the HMAC-SHA256 keyed-hashing algorithm
            final Mac mac_SHA256 = Mac.getInstance("HmacSHA256");

            // generate a key from the generator
            SecretKeySpec key = new SecretKeySpec(secreto.getBytes(), "HmacSHA256");

            mac_SHA256.init(key);

            // get the string as UTF-8 bytes
            byte[] b = mensaje.getBytes("UTF-8");

            // create a digest from the byte array
            byte[] digest = mac_SHA256.doFinal(b);

            return null;

        }catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm:" + e.getMessage());
            return null;
        }catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Encoding:" + e.getMessage());
            return null;
        }catch (InvalidKeyException e) {
            System.out.println("Invalid Key:" + e.getMessage());
            return null;
        }
    }
}