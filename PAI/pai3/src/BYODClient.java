import java.io.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;


public class BYODClient {
	



    public BYODClient() {
        try {
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 7070);

            // Crea un PrintWriter para enviar mensaje/MAC al servidor
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

	    	//NOMBRE DE USUARIO
            String username = JOptionPane.showInputDialog(null, "Introduzca su nombre de usuario:");
            output.println(username);
            
            //CONTRASEÑA
            String password = JOptionPane.showInputDialog(null, "Introduzca su contraseña:");
            output.println(password);
            
            //MENSAJE
            String msg = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");
            output.println(msg);
            
            // Importante para que el mensaje se envíe
            output.flush();
            // Crea un objeto BufferedReader para leer la respuesta del servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Lee la respuesta del servidor
            String respuesta = input.readLine();
            // Muestra la respuesta al cliente
            JOptionPane.showMessageDialog(null, respuesta);
            // Se cierra la conexion
            output.close();
            input.close();
            socket.close();
        } // end try
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
        // Salida de la aplicacion
        finally {
            System.exit(0);
        }
    }

    // ejecución del cliente de verificación de la integridad
    public static void main(String args[]) {
        new BYODClient();
    }
}
