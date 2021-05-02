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
//    private val chatmessage: EditText? = findViewById(R.id.chatmessage)
//    private val username: EditText? = findViewById(R.id.username)
//    private val send: Button? = findViewById(R.id.send)
//    private val chattext: TextView? = findViewById(R.id.chattext)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myWebSocketClient = MyWebSocketClient(URI("ws://10.0.2.2:8080"), Draft_17())
        myWebSocketClient.connect()
        //Elemente holen
        val send: Button = findViewById(R.id.send)
        val username: EditText = findViewById(R.id.username)
        val chatmessage: EditText = findViewById(R.id.chatmessage)

        send.setOnClickListener {
            val myUsername = username.text
            val myMessage = chatmessage.text
            myWebSocketClient.send("$myUsername: $myMessage")
        }

    }
    inner class MyWebSocketClient(serverUri: URI?, draft: Draft?) : WebSocketClient(serverUri, draft) {
        override fun onError(ex: Exception?) {
            Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
        }
        override fun onOpen(handshakedata: ServerHandshake?) {
            this@MainActivity.runOnUiThread {
                 Toast.makeText(this@MainActivity, "Opened", Toast.LENGTH_LONG).show()
            }
        }
        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            Log.d("MainActivity", "Closed")
        }
        override fun onMessage(message: String?) {
            val chattext: TextView = findViewById(R.id.chattext)
            val oldText = chattext.text
            chattext.text = "$message\n$oldText"
        }
    }

}