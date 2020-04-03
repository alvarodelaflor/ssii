import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class BYODServer {

    private ServerSocket serverSocket;

    // Constructor del Servidor
    public BYODServer() throws Exception {
        SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        serverSocket = (SSLServerSocket) socketFactory.createServerSocket(7070);
    }

    // Ejecución del servidor para escuchar peticiones de los clientes
    private void runServer() {
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
                // A continuación habría que calcular el mac del MensajeEnviado que podría ser
                String macdelMensajeEnviado = input.readLine();
                //mac del MensajeCalculado

//                if (macMensajeEnviado.equals(macdelMensajeCalculado)) {
                if (true) {
                    output.println("Mensaje enviado integro " + mensaje);
                } else {
                    output.println("Mensaje enviado no integro.");
                }
                output.close();
                input.close();
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    // Programa principal
    public static void main(String args[]) throws Exception {
        BYODServer server = new BYODServer();
        server.runServer();
    }
}
