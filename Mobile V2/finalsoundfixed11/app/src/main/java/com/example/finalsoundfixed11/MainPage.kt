package com.example.finalsoundfixed11
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
//start of  the Main class
class MainActivity: AppCompatActivity() {

    // Xml layout objects being declared

    private lateinit var menubutton: FloatingActionButton
    private lateinit var pausebutton: FloatingActionButton
    private lateinit var playbutton: FloatingActionButton
    private lateinit var listView: ListView
    private lateinit var logoutbutton: Button
    private lateinit var exitmenubutton: ImageButton
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit  var loginButton: Button
    private lateinit var usernotfound: TextView

    // Variables use for server communication and with server communication being declared

    private lateinit  var tempMp31: File
    private lateinit var tempMp32: File
    private lateinit var finaltempMp3: File
    private lateinit var fos: FileOutputStream
    private lateinit var fos1: FileOutputStream
    private lateinit var fis:FileInputStream
    private lateinit var response: String
    private var ifconnectiondone = false
    private lateinit var age:String
    private var connection1 =  false
    private var connection2 = false

    // Variables used in song playing being declared

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var songnames: List<String>
    private var ifconnectionfinished = true

    // onCreate is the Main function in the class

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sets the users view to the main_page xml layout file.

        setContentView(R.layout.main_page)

        // The initialization of all Xml layout objects.

        loginButton = findViewById(R.id.button22)
        usernotfound = findViewById(R.id.textView)
        username = findViewById(R.id.editTextTextPersonName)
        password = findViewById(R.id.editTextTextPersonpassword)
        playbutton = findViewById(R.id.playbutton)
        pausebutton= findViewById(R.id.pausebutton)
        menubutton= findViewById(R.id.menu_button)
        logoutbutton= findViewById(R.id.logoutbutton2)
        exitmenubutton = findViewById(R.id.exitmenubutton)
        listView = findViewById<ListView>(R.id.listview)

        // Sets up the action to be done everytime the login button is pressed.

        loginButton.setOnClickListener {
            if (username.text.toString().isNotEmpty()) {

                /* Thread creates a separate thread in which the actions within can be computed separately and asynchronous
                 to the main thread. The code is written inside the Thread is for connecting to the server
                 and sending the username and password as well as receiving a true/false if the username and password
                 are correct/incorrect and the reason why it is inside of the thread is because networking operations
                 are not allowed on the main Thread in newer versions of android.*/

                Thread {
                    val port = 1235

                    // Creates connection to server for logging in and establishes an input/output stream
                    // and stream writer as well as a buffered writer/reader.

                    val client = Socket("192.168.219.217", port)
                    val inputStreamReader = InputStreamReader(client.getInputStream())
                    val outputStreamWriter = OutputStreamWriter(client.getOutputStream())
                    val bufferedReader = BufferedReader(inputStreamReader)
                    val bufferedWriter = BufferedWriter(outputStreamWriter)
                    // Grabs the username inputted into the username box
                    var msgToSend = "*9#" + username.text.toString()
                    bufferedWriter.write(msgToSend)
                    bufferedWriter.newLine()
                    bufferedWriter.flush()
                    // Grabs the password inputted into the password box
                    msgToSend = "#8*" + password.text.toString()
                    Log.d("nothing", msgToSend)
                    bufferedWriter.write(msgToSend)
                    bufferedWriter.newLine()
                    bufferedWriter.flush()
                    // Receives the response from the server if the login information is correct
                    response = bufferedReader.readLine()
                    while (!response.equals("true") && !response.equals("false")) {
                        response = bufferedReader.readLine()
                    }
                    // If the login information is correct then it recieves the birth year of the account to then
                    // retrieve the song list that will be displayed
                    if(response.equals("true")){
                        age = bufferedReader.readLine()
                        while (age.isNullOrEmpty()) {
                            age = bufferedReader.readLine()
                        }

                        age = age.substring(0,4)

                    }



                    Log.d("something", response)
                    // Sets ifconnectiondone to true to allow the following while loop to terminate
                    // and use the received information
                    ifconnectiondone = true
                    // Closes all of the input and output streams and writers as well as closes the connection
                    bufferedWriter.write("bye")
                    bufferedWriter.newLine()
                    bufferedWriter.flush()
                    bufferedReader.close()
                    bufferedWriter.close()
                    inputStreamReader.close()
                    outputStreamWriter.close()
                    client.close()
                }.start()
                while (true) {
                    // waits until the connection is finished and then runs the function run
                    // which check if the response is "true" or "false" and then proceeds to break the connection
                    // and reset the boolean value ifconnectiondone so that the login can be used again
                    if (ifconnectiondone) {
                        run()
                        break
                        ifconnectiondone = false
                    }
                }
            }
        }
        // Sets up the mediaplayer which is then used to play the song
        mediaPlayer = MediaPlayer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            mediaPlayer.setAudioAttributes(audioAttributes)
        } else {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
        // Sets ifconnectionfinished to false for the playsong function
        ifconnectionfinished = false
        // Sets up all of the temporary files that will be used to store the separate song chunks and the song put together
        tempMp31 = File.createTempFile("LuisBlake", "mp3", cacheDir)
        tempMp32 = File.createTempFile("LuisBlake1","mp3",cacheDir)
        finaltempMp3 = File.createTempFile("LuisBlake6","mp3",cacheDir)
        // Sets the temporary files to be deleted when the app is completely closed
        tempMp31.deleteOnExit()
        tempMp32.deleteOnExit()
        finaltempMp3.deleteOnExit()
        // Sets up the onclicklisteners that lead to the functions for each button that can be pressed
        // aside from the logout button and login button
        menubutton.setOnClickListener {
            menubuttonAction()
        }
        exitmenubutton.setOnClickListener {
            exitmenubuttonAction()
        }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    // This function is responsible for making a connection to the server, sending the birth year of the user and
    // recieving the list of songs that will be displayed for playing
    private fun getsongnames(){
        // Creates the separate thread
        Thread{
            // Creates a connection to the server and sets up input/output streams and writers as well as the buffered writers
            val port = 1235
            val client = Socket("192.168.219.217", port)
            val inputStreamReader = InputStreamReader(client.getInputStream())
            val outputStreamWriter = OutputStreamWriter(client.getOutputStream())
            val bufferedReader = BufferedReader(inputStreamReader)
            val bufferedWriter = BufferedWriter(outputStreamWriter)

            // Declares and initializes the message that will be sent and sends it
            var msgToSend = "***" + age
            bufferedWriter.write(msgToSend)
            Log.d("thisis the message",msgToSend)
            bufferedWriter.newLine()
            bufferedWriter.flush()
            // Recieves the list of the song for the birth year on the users account
            response = bufferedReader.readLine()
            while(response.isNullOrEmpty()){
                response = bufferedReader.readLine()
            }
            Log.d("something",response)
            // Sets ifconnectionfinished to true to allow the following while loop to terminate and use
            // the recieved information
            ifconnectionfinished = true
            // Closes all of the input/output readers/writers and the buffered writes as well as closes the connection
            bufferedWriter.write("bye")
            bufferedWriter.newLine()
            bufferedWriter.flush()
            bufferedWriter.close()
            bufferedReader.close()
            outputStreamWriter.close()
            inputStreamReader.close()
            client.close()
        }.start()
        while(true){
            // Uses the information gathered from the connection and splits it into a list for
            // the playsong functon to use and make a visible clickable list it also resets the ifconnectionfinished boolean to false
            // so that the connection can be reused without causing an infinite loop
            if(ifconnectionfinished){
                songnames = response.split(",", ignoreCase = false, limit = 0)
                playsong(songnames)
                break
                ifconnectionfinished = false
            }
        }
    }

    /* The pausebutton_and_playbutton function initializes two onClickListeners to hide/show the pause/play button
    as well as pause/resume currently playing song. */
    private fun pausebutton_and_playbutton(){
        playbutton.hide()
        pausebutton.show()
        pausebutton.setOnClickListener{
            mediaPlayer?.pause()
            playbutton.show()
            pausebutton.hide()

        }
        playbutton.setOnClickListener{
            if(ifconnectionfinished){
                mediaPlayer.start()
                playbutton.hide()
                pausebutton.show()
            }



        }
    }
    /* The playsong function takes in an array of song names and makes a list of clickable objects
       * from which a song can be chosen and played.  */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun playsong(songnames:List<String>) {
        // Sets up the array adapter that will be used to set the list on the list view
        val arrayAdapter: ArrayAdapter<*>

        val layoutResourceId = R.layout.list_item

        arrayAdapter = ArrayAdapter(this, layoutResourceId, songnames.toList())

        listView.adapter = arrayAdapter
        // Sets up the set on item click listener which lets ever item in the list be clickable.
        listView.setOnItemClickListener { parent, _, position, _ ->
            // Checks to see if the last song has finished transferring over
            if(ifconnectionfinished){
                // Resets the mediaplayer and flips the positions of the play and pause button
                mediaPlayer?.release()
                playbutton.show()
                pausebutton.hide()
                // Gets the item that was clicked
                val selectedItem = parent.getItemAtPosition(position) as String
                val index = 0
                /*
                   Goes through the list comparing the item that was clicked with the item that it is currently on
                   once it finds the item it then proceeds to set up the media player again and starts
                   the Song_reciever function, from there once the Song_reciever function is finished it uses the set
                   file input stream from the function to be used by the media player. The media player then proceeds to prepare,
                   swap the positions of the play, and pause buttons and set several listeners to wait until the song is prepared.
                   Once the song is prepared it starts the mediaplayer which also plays the song, an on completion listener is also
                   set to reset the media player and swap the positions of the play and pause buttons once the song is completed
                */
                for (name in songnames) {
                    if (name == "$selectedItem") {
                        // Sets up the media player
                        mediaPlayer = MediaPlayer()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            val audioAttributes = AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                            mediaPlayer.setAudioAttributes(audioAttributes)
                        } else {
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                        }
                        // resets the ifconnectionfinished boolean to false
                        ifconnectionfinished = false
                        // Starts the retrieving of the song
                        Song_Reciever(name)
                        Log.d("this is to see if it","somethingsanhtiaosnh")
                        // While loop that waits for the transfer of the song to finish to set, play, and stop the media player
                        while(true){

                            if(ifconnectionfinished){
                                mediaPlayer.setDataSource(fis.fd)
                                mediaPlayer.prepareAsync()
                                pausebutton_and_playbutton()

                                mediaPlayer.setOnPreparedListener {
                                    mediaPlayer.start()
                                }
                                mediaPlayer.setOnCompletionListener {
                                    mediaPlayer.release()
                                    pausebutton.hide()
                                    playbutton.show()
                                }
                                break
                            }
                        }
                        Log.d("checkers","donerdone")
                    }
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    /*
       Requests each chunk of the song using the songname String to get the specific song from the server.
       From there is takes the chucks puts them together and set them into a file input stream
     */
    private fun Song_Reciever(songname:String){

        connection1 = false
        connection2 = false

        // Initializes the file output streams to write to temporary files
        fos = FileOutputStream(tempMp31)
        fos1 = FileOutputStream(tempMp32)


        // starts the separate thread
        Thread{
            // Sets up the server connection along side the input/output stream/stream writer and the buffered reader/writer
            val port = 1235
            val client = Socket("192.168.219.217", port)
            val outputStreamWriter = OutputStreamWriter(client.getOutputStream())
            val inputStream :InputStream = client.getInputStream()
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val bufferedWriter = BufferedWriter(outputStreamWriter)
            // Sets up the message to be sent
            val msgToSend = "###1" + songname
            bufferedWriter.write(msgToSend)
            bufferedWriter.newLine()
            bufferedWriter.flush()
            // Waits for the server to respond to send confirmation of connection and start receiving the song
            val buffer = ByteArray(16)
            var lengthofsong = bufferedReader.readLine()
            while(lengthofsong.isNullOrEmpty()){
                lengthofsong = bufferedReader.readLine()
            }
            bufferedWriter.write("recieved")
            bufferedWriter.newLine()
            bufferedWriter.flush()

            val dataInputStream = DataInputStream(inputStream)
            var lengthofbytesrecieved = 0;
            while (lengthofbytesrecieved <= Integer.parseInt(lengthofsong)) {
                //Log.d("sometihng","this is sometihng")
                val readbytes = dataInputStream.read(buffer)
                fos.write(buffer,0,readbytes)
                lengthofbytesrecieved += readbytes
            }
            Log.d("bytes",lengthofbytesrecieved.toString())
            connection1 = true
            Log.d("done","done")
            // Closes all of the buffered, inputstream/outputstream readers/writers and closes the connection
            bufferedWriter.close()
            bufferedReader.close()
            inputStreamReader.close()
            outputStreamWriter.close()
            client.close()
        }.start()
        // starts the separate thread
        Thread{
            // Sets up the server connection along side the input/output stream/stream writer and the buffered reader/writer
            val port = 1235
            val client = Socket("192.168.219.217", port)
            val outputStreamWriter = OutputStreamWriter(client.getOutputStream())
            val inputStream :InputStream = client.getInputStream()
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val bufferedWriter = BufferedWriter(outputStreamWriter)
            // Sets up the message to be sent
            val msgToSend = "###2" + songname
            bufferedWriter.write(msgToSend)
            bufferedWriter.newLine()
            bufferedWriter.flush()
            // Waits for the server to respond to send confirmation of connection and start receiving the song
            val buffer = ByteArray(16)
            var lengthofsong = bufferedReader.readLine()
            while(lengthofsong.isNullOrEmpty()){
                lengthofsong = bufferedReader.readLine()
            }
            // Writes back to the server when the length of the song in bytes is received
            bufferedWriter.write("recieved")
            bufferedWriter.newLine()
            bufferedWriter.flush()
            /*
               Declares and initializes the data input stream as well as declares and initializes the lengthofbytesrecieved integer to
               to keep track of the number of bytes recieved in total and compare it to the length of the chuck to know when to
               stop receiving information.
            */
            val dataInputStream = DataInputStream(inputStream)
            var lengthofbytesrecieved = 0;

            while (lengthofbytesrecieved <= Integer.parseInt(lengthofsong)) {
                // Reads the sent byte array and stores the number of bytes read from the array
                val readbytes = dataInputStream.read(buffer)
                // Uses the number of read bytes to write the number of bytes from the received bytes array to the first temporary file
                fos1.write(buffer, 0,readbytes)
                // Basic increment for the lengthofbytesrecieved variable
                lengthofbytesrecieved += readbytes
            }
            // Set connection to true to allow the while loop to know that the second chunk is done sending
            connection2 = true
            // Closes all input/output stream writers/readers and closes the connection
            bufferedWriter.close()
            bufferedReader.close()
            inputStreamReader.close()
            outputStreamWriter.close()
            client.close()
        }.start()
        /*
           Constantly checks if connection1 and connection2 are finish and when they are closes both outputstreams
           to the temporary files. Proceeding to make a byte array the length of the first and later the second
           chunk and store it in buffer to transfer the total amount of data to the main file that will
           store both chunks and be used  as the file input stream for the media player.
         */
        while(true){
            Log.d("soehtn",connection1.toString())
            Log.d("soehtn",connection2.toString())

            if(connection1 && connection2 ){
                // The closing of the file output streams of both temporary files
                fos.close()
                fos1.close()

                var bytesread : Int
                var buffer = ByteArray(tempMp31.length().toInt()-1)
                /*
                 The initialization of the file output stream that will be used to write both of the song chunks
                 into the final file to be used as the file input stream for the media player.
                 */
                fos = FileOutputStream(finaltempMp3)
                // The initialization of the file input stream to read the first chunk off of the first temporary file
                fis = FileInputStream(tempMp31)
                /*
                 The reading and storing of the information of the first file
                 into the byte array.
                 */
                fis.read(buffer)
                // The writing of the information from the buffer to the final file, to be file input stream.
                fos.write(buffer)
                // The initialization of the file input stream to read the first chunk off of the second temporary file
                fis = FileInputStream(tempMp32)
                // The making and storing of the byte array the size of the chunk of the first song chunk
                buffer = ByteArray(tempMp32.length().toInt())
                /*
                 The reading and storing of the information of the second file
                 into the byte array.
                 */
                fis.read(buffer)
                // The writing of the information from the buffer to the final file, to be file input stream.
                fos.write(buffer)


                // The closing of the file output stream used to write to the final file that would be used for playing
                fos.close()
                // Setting the final file to be the input stream that will be used by the media player
                fis = FileInputStream(finaltempMp3)
                // Setting the variable ifconnectionfinished to true to notify the while loop in playsong that the song has finished sending
                ifconnectionfinished = true
                Log.d("this is a tag",fis.toString())
                Log.d("ifconnectionfinished", ifconnectionfinished.toString())
                // Just to break the while loop that the code is in
                break
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun run() {
        Log.d("everything", response)
        // Set the text color of the user not found text view to red
        usernotfound.setTextColor(Color.parseColor("#ff0000"))
        /*
           If response is true then it will reset the string for response so that it can be used again
           then it will change the visibility of the login screen to invisible and change the visibility of the main page
           to visible as well as use the function getsongnames to aquire the list of song names that will be displayed
         */
        if (response == "true") {
            // Resets the response string
            response = ""
            // Changes the visibility of the login screen to invisible
            username.visibility  = View.INVISIBLE
            password.visibility = View.INVISIBLE
            loginButton.visibility = View.INVISIBLE
            // Resets the text in the usernotfound text view to be blank
            usernotfound.text = ""
            // Changes the visibility of the main page to visible
            playbutton.show()
            listView.visibility = View.VISIBLE
            menubutton.show()
            // Runs the getsongsnames function to get the song names that will later be displayed
            getsongnames()

            /*
             Checks to see if response is false if it is it sets the text of the usernotfound
             text view to "Username or password is incorrect"
             */
        } else if (response == "false") {
            // The setting of the text within the text view
            usernotfound.text = "Username or password is incorrect"
        }
    }
    /*
       The exitmenubuttonAction function is meant to set the pause or play button based on if the song
       is playing at that moment while setting the visibility of the mainpage visible while
       setting the visibility of the menu to invisible
     */
    private fun exitmenubuttonAction(){
        // Checks if the song is currently playing and shows either the pause or play button respectively
        if(mediaPlayer.isPlaying) {
            pausebutton.show()
        }else{
            playbutton.show()
        }
        // Makes the menu invisible
        logoutbutton.visibility= View.INVISIBLE
        exitmenubutton.visibility= View.INVISIBLE
        // Makes the main page visible
        listView.visibility = View.VISIBLE
        menubutton.show()
    }
    /*
       This function is made so that when the menu button is pressed the main page will disappear and the
       menu will appear with working buttons.
     */
    private fun menubuttonAction(){
        // Checks if the pause button is showing if it is then it hides it other wise it hides the play button
        if(pausebutton.isShown) {
            pausebutton.hide()
        }else{
            playbutton.hide()
        }
        // Sets the visibility of the main page to invisible
        menubutton.hide()
        listView.visibility = View.INVISIBLE
        // Sets the visibility of the menu to visible
        logoutbutton.visibility = View.VISIBLE
        exitmenubutton.visibility = View.VISIBLE
        /*
            This set the on click listener so that when the logout button is pressed the mediaplayer
            is released even if a song is currently playing it also changes the visibility of the
            menu to invisible while changing the visibility of the login screen to visible.
         */
        logoutbutton.setOnClickListener {
            // The releasing of the mediaplayer
            mediaPlayer.release()
            // Changing the visibility of the menu to invisible
            exitmenubutton.visibility = View.INVISIBLE
            logoutbutton.visibility  = View.INVISIBLE
            // Changing the visibility of the login screen to visible
            loginButton.visibility = View.VISIBLE
            password.visibility = View.VISIBLE
            username.visibility = View.VISIBLE
            // Resting all necessary connection variables to false
            ifconnectiondone = false
            ifconnectionfinished = false
            // Clearing the text off of the text boxes from the last login
            username.text.clear()
            password.text.clear()
        }
    }
}