import java.io.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;


public class generator_user {
	
    public static void main(String[] args) throws IOException {
        File archivo = new File("user.txt");
        BufferedWriter bw = null;
        bw = new BufferedWriter(new FileWriter(archivo));
    	for (int i = 0; i < 300; i++) {
    		bw.write("prueba"+i + "||" + "prueba"+i + "\n");            	
		}
        bw.close();
    }
}
