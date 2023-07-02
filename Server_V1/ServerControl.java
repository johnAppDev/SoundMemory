import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//This class manages the individual connections
public class ServerControl extends Thread {
    Socket socket = null;
    User currentUser;

    // The connection made by main is handed over to ServerControl
    public ServerControl(Socket socket) {
        this.socket = socket;
    }

    // this splits the song in half
    public static List<byte[]> songSplitter(String filePath, int chunkSize) {
        List<byte[]> chunks = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(filePath)) {
            byte[] buffer = new byte[chunkSize];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] chunk = Arrays.copyOf(buffer, bytesRead);
                chunks.add(chunk);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return chunks;
    }

    // Thread starts
    public void run() {
        FileInputStream fileInputStream = null;
        InetAddress inetAddress = socket.getInetAddress();
        InetAddress ip = null;
        DataOutputStream dataOutputStream = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        User activeUser = null;
        String name = "";
        String pass = "";
        String newname = "";
        String newpass = "";
        int yearOfBirth = 1;
        DatagramSocket dSocket;
        DatagramPacket dPacket;

        String test = "Hello, Packet Sent";

        try {
            System.out.println("Connected on thread: " + this);

            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            ip = InetAddress.getByName(inetAddress.getHostAddress());

            SQLCaller sqlmanager = new SQLCaller();

            InetAddress inetAddress2 = InetAddress.getByName("localhost");
            SocketAddress socketAddress = new InetSocketAddress(inetAddress2, 1235);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            String msgFromClient = "";
            // loop handles requests from client
            while (true) {

                msgFromClient = bufferedReader.readLine();

                if (msgFromClient != null) {
                    System.out.println("Client: " + msgFromClient + " From: " + this + " ");
                    // outputs the song names when summoned by the prefix ***
                    if (msgFromClient.substring(0, 3).equals("***")) {

                        String birthyear = msgFromClient.substring(3);

                        String songs = SQLCaller.getSongsNames(Integer.parseInt(birthyear));

                        bufferedWriter.write(songs);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        System.out.println(songs);

                    }
                    // Client calls for a song; Song is sent in two chunks
                    if (msgFromClient.substring(0, 3).equals("###")) {
                        fileInputStream = new FileInputStream("Songs/" + msgFromClient.substring(4) + ".mp3");
                        File songFile = new File("Songs/" + msgFromClient.substring(4) + ".mp3");
                        int fileSizeInBytes = (int) (songFile.length() / 2);
                        // adds 1 to fileSizeInBytes if it is not even; this is to prevent bytes from
                        // being lost

                        // chunks holds the two halves of the song split into byte arrays
                        List<byte[]> chunks = songSplitter("Songs/" + msgFromClient.substring(4) + ".mp3",
                                fileSizeInBytes);
                        // sends the first half of the song when called by client
                        if (msgFromClient.substring(0, 4).equals("###1")) {

                            fileSizeInBytes = chunks.get(0).length;
                            ByteArrayInputStream chunkreader = new ByteArrayInputStream(chunks.get(0), 0,
                                    fileSizeInBytes);
                            // sends the size of the half to the client, so that it can be properly handled.
                            bufferedWriter.write(Integer.toString(fileSizeInBytes));
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            // sent as 16 bytes at a time
                            byte[] buffer = new byte[16];
                            // response used as a delay; Synchronizes client with server; Waits until client
                            // is ready
                            String response = bufferedReader.readLine();
                            while (response.equals("")) {
                                response = bufferedReader.readLine();
                                System.out.println(response);
                            }
                            // sends bytes to client
                            while (chunkreader.available() != 0) {
                                chunkreader.read(buffer);

                                dataOutputStream.write(buffer);

                            }
                            System.out.println("done");
                            break;
                            // Second part does the same as the first, but it sends the second half
                        } else if (msgFromClient.substring(0, 4).equals("###2")) {
                            fileSizeInBytes = chunks.get(1).length;
                            ByteArrayInputStream chunkreader = new ByteArrayInputStream(chunks.get(1), 0,
                                    fileSizeInBytes);
                            bufferedWriter.write(Integer.toString(fileSizeInBytes));
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            byte[] buffer = new byte[16];
                            String response = bufferedReader.readLine();
                            while (response.equals("")) {
                                response = bufferedReader.readLine();
                                System.out.println(response);
                            }

                            while (chunkreader.available() != 0) {
                                chunkreader.read(buffer);

                                dataOutputStream.write(buffer);

                            }
                            System.out.println("done");
                            break;
                        }
                    }
                    // user info edited
                    if (msgFromClient.substring(0, 3).equals("^_^")) {
                        String patientinformation = msgFromClient.substring(3);
                        String[] splitinformation = patientinformation.split("`");
                        System.out.println(newname);
                        SQLCaller.addpatientinformation(splitinformation, newname);

                    }
                    // name to add is assigned by client
                    if (msgFromClient.substring(0, 4).equals("!:)1")) {
                        newname = msgFromClient.substring(4);
                    }
                    // pass to add is assigned by client
                    if (msgFromClient.substring(0, 4).equals("!:)2")) {
                        newpass = msgFromClient.substring(4);
                    }
                    //
                    if (!newname.isEmpty() && !newpass.isEmpty()) {

                        Boolean nameBool = SQLCaller.checkName(newname);
                        // if the name is not already used, and the password is not equal to null, then
                        // add the user with a default yearOfBirth(to be changed later)
                        if (!nameBool && newpass != null) {
                            bufferedWriter.write("good");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            System.out.println("addUser");
                            SQLCaller.addUser(newname, newpass, yearOfBirth);
                            System.out.println("iashoentast");
                        } else {
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            bufferedWriter.write("username");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }
                    }
                    // getting name to check
                    if (msgFromClient.substring(0, 3).equals("*9#")) {
                        name = msgFromClient.substring(3);
                        newname = msgFromClient.substring(3);
                        // getting pass to check
                    } else if (msgFromClient.substring(0, 3).equals("#8*")) {
                        pass = msgFromClient.substring(3);
                    }
                    // checking name and pass combination and sending output to client
                    if (!name.equals("") && !pass.equals("")) {
                        boolean check = SQLCaller.checkPass(pass, name);
                        System.out.println(check);
                        bufferedWriter.write(check + "");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                        if (check) { // if the check is true, it will log in
                            currentUser = SQLCaller.createUser(name);
                            System.out.println("soentiasenhtiaoshtn");
                            bufferedWriter.write(Integer.toString(currentUser.getBirthYear()));
                            // BufferedWriter currentprofilewriter = new BufferedWriter(new FileWriter(
                            // "C:\\Users\\Luis\\Desktop\\TESTSERVER-and client code\\currentprofile.txt"));
                            // System.out.println(Integer.toString(currentUser.getBirthYear()));
                            // sending info to client
                            bufferedWriter.write(currentUser.getName() + "`" + Integer.toString(currentUser.getheight())
                                    + "`" + Integer.toString(currentUser.getweight()) + "`" + currentUser.getBirthDate()
                                    + "`" + currentUser.getcaretakername() + "`" + currentUser.getillnesses() + "`"
                                    + currentUser.getallergies() + "`" + currentUser.getadditionalnotes() + "`"
                                    + currentUser.getemergencycontacts() + "`"
                                    + Integer.toString(currentUser.getroomnumber()) + "`"
                                    + currentUser.getcurrentmedication());
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            System.out.println(currentUser.getName() + "`" + Integer.toString(currentUser.getheight())
                                    + "`" + Integer.toString(currentUser.getweight()) + "`" + currentUser.getBirthDate()
                                    + "`" + currentUser.getcaretakername() + "`" + currentUser.getillnesses() + "`"
                                    + currentUser.getallergies() + "`" + currentUser.getadditionalnotes() + "`"
                                    + currentUser.getemergencycontacts() + "`"
                                    + Integer.toString(currentUser.getroomnumber()) + "`"
                                    + currentUser.getcurrentmedication());

                            name = "";
                            pass = "";

                        }
                    }
                }

                bufferedWriter.newLine();

                bufferedWriter.flush();

                if (msgFromClient.equalsIgnoreCase("BYE")) {

                    break;
                }

            }
            System.out.println("Closing...");
            inputStreamReader.close();
            outputStreamWriter.close();
            bufferedReader.close();
            bufferedWriter.close();
            socket.close();
            // break;

        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

}
