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
        
    ServerControl Thread_1 = new ServerControl(1234);
    ServerControl Thread_2 = new ServerControl(1235);
    Thread_1.start();
    Thread_2.start();

    /*Socket socket =  null;
    InputStreamReader inputStreamReader  = null;
    OutputStreamWriter outputStreamWriter = null;
    BufferedReader bufferedReader = null;
    BufferedWriter bufferedWriter = null;
    ServerSocket serverSocket = new ServerSocket(1234);
 


    while(true){

        try{
            socket = serverSocket.accept();
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
           // Scanner sc = new Scanner(System.in);
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write("Connected!");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            while(true){
                String msgFromClient = bufferedReader.readLine();
                System.out.println("Client: "+ msgFromClient);
               // String msg = sc.nextLine();
                bufferedWriter.write("MESSAGE RECIEVED");
                bufferedWriter.newLine();
                bufferedWriter.flush();
                if(msgFromClient.equalsIgnoreCase("BYE"))
                    break;
            }
            socket.close();
            inputStreamReader.close();
            outputStreamWriter.close();
            bufferedReader.close();
            bufferedWriter.close();


        }catch(IOException e){
            e.printStackTrace();
        }
    }



    
   */
}
}
