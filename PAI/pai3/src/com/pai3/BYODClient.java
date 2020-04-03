package com.pai3;

import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;
import javax.net.*;

public class BYODClient {

    public BYODClient() {
        try {
            SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
            Socket socket = (Socket) socketFactory.createSocket("localhost", 7070 );
                    // Crea un PrintWriter para enviar mensaje/MAC al servidor
                    PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String userName = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");
            // Envío del mensaje al servidor
            String mensaje = "Msg1";
            output.println(mensaje);
            // Habría que calcular el correspondiente MAC con la clave compartida por servidor/cliente
            String macdelMensaje = "MacdelMensaje";
            output.println(macdelMensaje);
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

    public static void main(String args[]) throws InvalidKeyException, NoSuchAlgorithmException {
        new BYODClient();
    }
}
