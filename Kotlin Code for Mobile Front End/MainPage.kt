package com.example.finalsoundfixed11

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainPage: AppCompatActivity() {
    private lateinit var menubutton: FloatingActionButton
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var pausebutton: FloatingActionButton
    private lateinit var playbutton: FloatingActionButton
    private lateinit var listView: ListView
    private lateinit var logoutbutton: Button
    private lateinit var exitmenubutton: ImageButton
    private lateinit var songnames: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        mediaPlayer = MediaPlayer.create(this, resources.getIdentifier("song", "raw", packageName))
        playbutton = findViewById(R.id.playbutton)
        pausebutton= findViewById(R.id.pausebutton)
        menubutton= findViewById(R.id.menu_button)
        logoutbutton= findViewById(R.id.logoutbutton2)
        exitmenubutton = findViewById(R.id.exitmenubutton)

        menubutton.setOnClickListener {
            menubuttonAction()
        }
        exitmenubutton.setOnClickListener {
            exitmenubuttonAction()
        }
        songnames = arrayOf(
            "a_bobbydarinmacktheknife",
            "b_animalshouseoftherisingsun",
            "c_beachboyswouldntitbenice",
            "d_abba_waterloo",
            "e_abbagimmegimmegimme"
        )
        playsong(songnames,"nothing")
    }

    /* The pausebutton_and_playbutton function initializes two onClickListeners to hide/show the pause/play button
    as well as pause/resume currently playing song. */

    private fun pausebutton_and_playbutton(){

        playbutton.hide()
        pausebutton.show()
        pausebutton.setOnClickListener{
            mediaPlayer.pause()
            playbutton.show()
            pausebutton.hide()
            playbutton.setOnClickListener{
                if(mediaPlayer != null){
                    mediaPlayer.start()
                }

                playbutton.hide()
                pausebutton_and_playbutton()
            }
        }
    }

    /* The playsong function takes in an array of song names and makes a list of clickable objects
       * from which a song can be chosen and played.  */
    private fun playsong(songnames:Array<String>, lastchosensong:String) {
        val arrayAdapter: ArrayAdapter<*>
        listView = findViewById<ListView>(R.id.listview)
        arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, songnames
        )
        listView.adapter = arrayAdapter
        if(lastchosensong == "nothing"){
            listView.setOnItemClickListener { parent, _, position, _ ->
                val selectedItem = parent.getItemAtPosition(position) as String
                val index = 0
                for (name in songnames) {
                    if (name == "$selectedItem") {

                        mediaPlayer =
                            MediaPlayer.create(this, resources.getIdentifier(name, "raw", packageName))
                        pausebutton_and_playbutton()
                        mediaPlayer?.setOnPreparedListener {
                            mediaPlayer?.start()
                        }
                        val currentListview = findViewById<ListView>(R.id.listview)
                        currentListview.adapter = arrayAdapter

                        currentListview.setOnItemClickListener { parent, _, position, _ ->
                            mediaPlayer?.release()
                            pausebutton.hide()
                            playbutton.show()
                            playsong(songnames,(parent.getItemAtPosition(position) as String))
                        }

                        mediaPlayer?.setOnCompletionListener {
                            mediaPlayer?.release()
                        }

                    }
                }



            }
        }else{
            for (name in songnames) {
                if (name == lastchosensong) {

                    mediaPlayer =
                        MediaPlayer.create(this, resources.getIdentifier(name, "raw", packageName))
                    pausebutton_and_playbutton()
                    mediaPlayer?.setOnPreparedListener {
                        mediaPlayer?.start()
                    }
                    val currentListview = findViewById<ListView>(R.id.listview)
                    currentListview.adapter = arrayAdapter

                    currentListview.setOnItemClickListener { parent, _, position, _ ->
                        mediaPlayer?.release()
                        pausebutton.hide()
                        playbutton.show()
                        playsong(songnames,(parent.getItemAtPosition(position) as String))
                    }

                    mediaPlayer.setOnCompletionListener {
                        mediaPlayer.release()
                    }

                }
            }

        }

    }


    private fun exitmenubuttonAction(){
        if(mediaPlayer.isPlaying) {
            pausebutton.show()
        }else{
            playbutton.show()
        }
        menubutton.show()
        logoutbutton.visibility= View.INVISIBLE
        exitmenubutton.visibility= View.INVISIBLE
        listView.visibility = View.VISIBLE

    }


    private fun menubuttonAction(){
        val backtologinscreen = Intent(this, MainActivity::class.java)
        if(pausebutton.isShown) {
            pausebutton.hide()
        }else{
            playbutton.hide()
        }

        menubutton.hide()
        logoutbutton.visibility = View.VISIBLE
        exitmenubutton.visibility = View.VISIBLE
        listView.visibility = View.INVISIBLE
        logoutbutton.setOnClickListener {

            startActivity(backtologinscreen)
            mediaPlayer.release()
        }

    }

}