import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
class Main{
    public static void main(String[] args) {
        Socket client = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter =null;
        Scanner role = new Scanner(System.in);
        String text = role.nextLine();
        int port;
        if (text.equals("p")){
                port = 1234;
        }else{
            port = 1235;
        }
       
            
    try{
        client = new Socket("localhost", port);
        inputStreamReader = new InputStreamReader(client.getInputStream());
        outputStreamWriter = new OutputStreamWriter(client.getOutputStream());
        bufferedReader = new BufferedReader(inputStreamReader);
        bufferedWriter =  new BufferedWriter(outputStreamWriter);
        Scanner sc = new Scanner(System.in);
        
        while(true){
            
                String msgToSend = sc.nextLine();
                if(msgToSend.equalsIgnoreCase("Bye")){
                    break;
                }
                bufferedWriter.write(msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                System.out.println(bufferedReader.readLine());

            
        }

    
    } catch (IOException e){
    e.printStackTrace();
    }finally{
        try {
        if(client != null)
            client.close();
        if(inputStreamReader != null)
            inputStreamReader.close();
        if(outputStreamWriter != null)
        outputStreamWriter.close();
        if (bufferedReader!= null)
        bufferedReader.close();
        if(bufferedWriter != null)
        bufferedWriter.close();
        
        
        
          
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
        
}
}
