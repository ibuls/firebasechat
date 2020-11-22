package com.firestorechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.firestorechat.databinding.ActivityMainBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private var anotherUserId: Int = 2
    private var userId: Int = 1
    lateinit var db: FirebaseFirestore
    private var binding: ActivityMainBinding? = null

    lateinit var adapter: FirebaseChatsAdapter
    var listChats: ArrayList<ChatModel.ChatMessage> = ArrayList()
    val ROOT_NODE = "chat"
    val LAST_MESSAGE_NODE = "lastMessage"
    val MESSAGES_NODE = "listMessages"


    var chatReference: DocumentReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding?.apply {
            etUserId.setText(1.toString())
            etAnotherUserId.setText(2.toString())

            adapter = FirebaseChatsAdapter(this@MainActivity, listChats)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }
        db = Firebase.firestore

        chatReference = initiateChat()
        adapter.userId = userId
        initChatListener()

        setupClickListeners()

    }

    private fun initChatListener() {

        chatReference?.collection(MESSAGES_NODE)
            ?.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    Toast.makeText(this@MainActivity, "onEvent called!", Toast.LENGTH_SHORT).show()
                    listChats.clear()



                    value?.toObjects(ChatModel.ChatMessage::class.java)?.let {
                        listChats.addAll(it)
                    }

                   /* value?.documents?.forEach { document ->
                        if (document.exists()) {

                            Toast.makeText(this@MainActivity, "onEvent called! for id : ${?.chatId}", Toast.LENGTH_SHORT).show()

                           *//* document.toObject(ChatModel.ChatMessage::class.java)?.let {
                                listChats.add(it)

                            }*//*
                        }
                    }*/

                    adapter.notifyDataSetChanged()
                }

            })
    }

    private fun setupClickListeners() {
        binding?.apply {
            btnInitiate.setOnClickListener {
                chatReference = initiateChat()

                if (chatReference != null) {
                    Toast.makeText(
                        this@MainActivity,
                        "Chat Initiated Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    adapter.userId = userId
                } else {
                    Toast.makeText(this@MainActivity, "Chat Initiation failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            btnSend.setOnClickListener {

                if (chatReference == null) {
                    Toast.makeText(
                        this@MainActivity,
                        "Please initiate the chat",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                sendChatMessage(etMessage.text.toString())

            }
        }
    }

    fun initiateChat(): DocumentReference {
        userId = binding?.etUserId?.text?.toString()?.toInt() ?: 1
        anotherUserId = binding?.etAnotherUserId?.text?.toString()?.toInt() ?: 2

        val chatSuffix =
            if (userId > anotherUserId) anotherUserId.toString() + "_" + userId.toString() + "_" else userId.toString() + "_" + anotherUserId.toString() + "_"

        val chatId = "chat_${chatSuffix}"


        val user1 = ChatModel.Participant(userId, "User Fname", "User Lname")
        val user2 = ChatModel.Participant(userId, "Another User Fname", "Another User Lname")

        val chatModel = ChatModel(
            chatId, listOf(user1, user2),
            ChatModel.ChatMessage(0, -1, 0, "")
        )

        val chatRef = db.collection(ROOT_NODE).document(chatId)

        chatRef.set(chatModel)
        return chatRef
    }


    fun sendChatMessage(message: String) {
        if (message.trim().length == 0) {
            binding?.etMessage?.setText("")
            return
        }

        val message = ChatModel.ChatMessage(chatId, userId, System.currentTimeMillis(), message)

        chatReference?.update(LAST_MESSAGE_NODE, message)
        chatReference?.collection(MESSAGES_NODE)?.add(message)?.addOnSuccessListener { document ->

            Toast.makeText(this, "Send btn On Success Called..", Toast.LENGTH_SHORT).show()
            listChats.add(message)
            adapter.notifyDataSetChanged()

        }?.addOnFailureListener {
            Toast.makeText(this, "Send btn On Failed Called..", Toast.LENGTH_SHORT).show()

        }


        binding?.etMessage?.setText("")
    }

    var chatId: Long = 0
        get() = userId + System.currentTimeMillis()


    override fun onDestroy() {
        binding?.unbind()
        binding = null
        super.onDestroy()

    }
}