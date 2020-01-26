package com.example.btuchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val mAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference


    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MessageViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth.addAuthStateListener {
            if (it.currentUser == null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = MessageViewAdapter()

        recyclerView = findViewById<RecyclerView>(R.id.chatView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        initMessageListener()
        messageSendBtn.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.messanger_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutBtn -> {
                mAuth.signOut();
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        val message = Message()
        message.sender = mAuth.currentUser?.email
        message.message = messageBox.text.toString()

        database.child("Messages").child(UUID.randomUUID().toString()).setValue(message)
    }

    private fun initMessageListener() {
        database.child("Messages").addValueEventListener((object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messages = ArrayList<Message>()
                for(messageSnapshot in dataSnapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        messages.add(message)
                    }
                }
                viewAdapter.addMessage(messages)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(MainActivity::getLocalClassName.toString(), databaseError.message)
            }
        }))
    }
}
