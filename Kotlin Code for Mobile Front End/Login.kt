package com.example.finalsoundfixed11


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler

import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.util.concurrent.Executor
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {
    private lateinit var username:EditText
    private lateinit var password:EditText
    private var handler: Handler = Handler()
    private var response:String = ""
    private lateinit var usernotfound:TextView

    private fun run() {
        usernotfound.setTextColor(Color.parseColor("#ff0000"))
        if(response == "true" ){
            startActivity(Intent(this, MainPage::class.java))
        }else if(response == "false"){
            usernotfound.text = "Username or password is incorrect"
        }else{
            usernotfound.text = "Server error"
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginButton: Button = findViewById(R.id.button2)

        loginButton.setOnClickListener {

            usernotfound= findViewById(R.id.textView)

            username = findViewById(R.id.editTextTextPersonName)
            password = findViewById(R.id.editTextTextPersonpassword)
            if(username.text.toString().isNotEmpty()){

                Thread{
                    val port = 1235
                    val client = Socket("107.196.203.39", port)
                    val inputStreamReader = InputStreamReader(client.getInputStream())
                    val outputStreamWriter = OutputStreamWriter(client.getOutputStream())
                    val bufferedReader = BufferedReader(inputStreamReader)
                    val bufferedWriter = BufferedWriter(outputStreamWriter)
                    var msgToSend = "*9#" + username.text.toString()
                    bufferedWriter.write(msgToSend)
                    bufferedWriter.newLine()
                    bufferedWriter.flush()
                    msgToSend="#8*" + password.text.toString()
                    Log.d("nothing",msgToSend)
                    bufferedWriter.write(msgToSend)
                    bufferedWriter.newLine()
                    bufferedWriter.flush()
                    response = bufferedReader.readLine()
                    while(response != "true" && response != "false"){
                        response = bufferedReader.readLine()

                    }

                    Log.d("something",response)
                    client.close()
                    inputStreamReader.close()
                    outputStreamWriter.close()
                    bufferedReader.close()
                    bufferedWriter.close()

                }.start()
                handler.postDelayed(Runnable{ run() },1000)
            }
        }
    }
}

/* val MainPage = Intent(this, MainPage::class.java)

 val allProfiles: String
 val Profiles: InputStream = assets.open("profiles.txt")
 val size: Int = Profiles.available()
 val buffer = ByteArray(size)
 var profile_names_and_songlist = mutableListOf<String>()
 Profiles.read(buffer)
 allProfiles = String(buffer)
 var numlines = 0
 val lengthofstring = allProfiles.length
 var index = 0
 for (i in allProfiles) {
     if (i == '\n') {
         numlines++
     }
 }
 numlines++
 for (i in 0 until numlines) {
     var temp = ""
     while (index < lengthofstring && allProfiles[index] != '\n') {
         temp += allProfiles[index].toString()
         index++
     }
     profile_names_and_songlist.add(temp)
     if (index <= lengthofstring - 1) {
         index++
     }
 }
 if (username.text.toString().isNotEmpty()) {
     something()
     for (i in 0 until profile_names_and_songlist.size) {
         val temp = profile_names_and_songlist[i]
         index = 0
         var current_name = ""
         while (temp[index].toString() != ",") {
             current_name += temp[index].toString()
             index++
         }
         if (current_name == username.text.toString()) {
             finish()
             startActivity(MainPage)
         }
     }
 }

*/






