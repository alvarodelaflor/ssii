package com.pai3;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;

public class BYODServer {

    private static final String CORRECT_USER_NAME = "Rafael";
    private static final String CORRECT_PASSWORD = "D23icOp.78";

    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException,
            InterruptedException {
        // espera conexiones del cliente y comprueba login
        SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        // crea Socket de la factoría
        SSLServerSocket serverSocket = (SSLServerSocket) socketFactory.createServerSocket(7070);

        while (true) {

            try {
                System.err.println("Esperando conexiones..");

                Socket socket = serverSocket.accept();

                // abre BufferedReader para leer datos del cliente
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                // abre PrintWriter para enviar datos al cliente
                PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                String userName = input.readLine();
                String password = input.readLine();
                if (userName.equals(CORRECT_USER_NAME)
                        && password.equals(CORRECT_PASSWORD)) {
                    output.println("Bienvenido, " + userName);
                } else {
                    output.println("Login Fallido.");
                }

                output.close();
                input.close();
                socket.close();

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } // end while
    }
    //serverSocket.close();
}
