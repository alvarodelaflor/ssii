import java.io.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;


public class BYODClient2 {

    private static final String[] protocols = new String[]{"TLSv1.3"};
    private static final String[] cipher_suites = new String[]{"TLS_AES_128_GCM_SHA256"};


    public BYODClient2(String user, String pass, String message) {
        try {
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) socketFactory.createSocket("90.69.187.8", 7070);
            socket.setEnabledProtocols(protocols);
            socket.setEnabledCipherSuites(cipher_suites);
            // Crea un PrintWriter para enviar mensaje/MAC al servidor
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            //NOMBRE DE USUARIO
//            String username = JOptionPane.showInputDialog(null, "Introduzca su nombre de usuario:");
            output.println(user);

            //CONTRASEÑA
//            String password = JOptionPane.showInputDialog(null, "Introduzca su contraseña:");
            output.println(pass);

            //MENSAJE
//            String msg = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");
            output.println(message);

            // Importante para que el mensaje se envíe
            output.flush();
            // Crea un objeto BufferedReader para leer la respuesta del servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Lee la respuesta del servidor
            //String respuesta = input.readLine();
            // Muestra la respuesta al cliente
            //JOptionPane.showMessageDialog(null, respuesta);
            // Se cierra la conexion
            output.close();
            input.close();
            socket.close();
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
        // Salida de la aplicacion
//        finally {
//            System.exit(0);
//        }
    }

    // ejecución del cliente de verificación de la integridad
    public static void main(String args[]) {
    	Instant start = Instant.now();
    	for (int i = 0; i <= 299; i++) {
        	String user = "prueba" + i;
        	String pass = "prueba" + i;
        	String msg = "Mensaje del usuario " + i;
        	
        	new BYODClient2(user,pass,msg);
		}
    	Instant end = Instant.now();
    	Duration interval = Duration.between(start, end);
    	System.err.println("Execution time in seconds: " + 	interval.getSeconds());
    }
}
