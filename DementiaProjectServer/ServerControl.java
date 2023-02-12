import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerControl extends Thread{
    private int port;
    public ServerControl(int port){
        this.port = port;
    }
    public ServerControl(){
        port = 1234;
    }
    
    Socket socket =  null;
    InputStreamReader inputStreamReader  = null;
    OutputStreamWriter outputStreamWriter = null;
    BufferedReader bufferedReader = null;
    BufferedWriter bufferedWriter = null;
    private static String ms = "";
 

 public void run(){
    
    while(true){

        try{
            ServerSocket serverSocket = new ServerSocket(port);
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
                ms = msgFromClient;
                System.out.println("Client: "+ msgFromClient + " From: " + this);
               // String msg = sc.nextLine();
                bufferedWriter.write( ms);
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
            
        }
    }
 }
 
} 

