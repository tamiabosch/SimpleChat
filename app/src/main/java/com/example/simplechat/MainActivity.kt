package com.example.simplechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft
import org.java_websocket.drafts.Draft_17
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class MainActivity : AppCompatActivity() {
    private lateinit var chatmessage: EditText
    private lateinit var username: EditText
    private lateinit var send: Button
    private lateinit var chattext: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Websocket öffnen
        val myWebSocketClient = MyWebSocketClient(URI("ws://10.0.2.2:8080"), Draft_17())
        myWebSocketClient.connect()

        //Elemente holen
        chatmessage = findViewById(R.id.chatmessage)
        send = findViewById(R.id.send)
        username = findViewById(R.id.username)
        chattext = findViewById(R.id.chattext)

        this.send.setOnClickListener {
            val myUsername = this@MainActivity.username.text
            val myMessage = this@MainActivity.chatmessage.text
            Log.d("send", "clicked")
            myWebSocketClient.send("$myUsername: $myMessage")
        }

    }

    inner class MyWebSocketClient(serverUri: URI?, draft: Draft?) : WebSocketClient(serverUri, draft) {
        override fun onError(ex: Exception?) {
            Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
        }
        override fun onOpen(handshakedata: ServerHandshake?) {
            this@MainActivity.runOnUiThread {
                Toast.makeText(this@MainActivity, "Opened connection", Toast.LENGTH_LONG).show()
                Log.d("MainActivity", "Opened")
            }
        }
        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            Log.d("MainActivity", "Closed")
        }
        override fun onMessage(message: String?) {
            val chattext: TextView = findViewById(R.id.chattext)
            val oldText = chattext.text
            this@MainActivity.chattext.text = "$message\n$oldText"

        }
    }

}