import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(1235);
        } catch (IOException e) {

            e.printStackTrace();
            System.out.println("Server error");
        }
        while (true) {
            try {
                socket = serverSocket.accept();
                ServerControl st = new ServerControl(socket);
                st.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
