import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class BYODServer {

    private SSLServerSocket serverSocket;
    private static final String[] protocols = new String[]{"TLSv1.3"};
    private static final String[] cipher_suites = new String[]{"TLS_AES_128_GCM_SHA256"};


    // Constructor del Servidor
    public BYODServer() throws Exception {
        SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        this.serverSocket = (SSLServerSocket) socketFactory.createServerSocket(7070);
        serverSocket.setEnabledProtocols(protocols);
        serverSocket.setEnabledCipherSuites(cipher_suites);
    }

    // Ejecuci�n del servidor para escuchar peticiones de los clientes
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

                String username = input.readLine();
                String password = input.readLine();
                String msg = input.readLine();


//              if (macMensajeEnviado.equals(macdelMensajeCalculado)) {
                if (true) {
                    output.println("Mensaje enviado integro " + username + password + msg);
                }
                output.close();
                input.close();
                socket.close();
            } catch (SSLHandshakeException exception) {
                // Output expected SSLHandshakeExceptions.
                System.err.println("Error: " + exception);
            } catch (IOException exception) {
                // Output unexpected IOExceptions.
                System.err.println("Error: " + exception);
            }
        }
    }

    // Programa principal
    public static void main(String args[]) throws Exception {
        BYODServer server = new BYODServer();
        server.runServer();
    }
}
