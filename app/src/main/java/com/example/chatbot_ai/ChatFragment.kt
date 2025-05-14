package com.example.chatbot_ai

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var usernameTextView: TextView
    private lateinit var chatAdapter: ChatAdapter
    private val messagesList = mutableListOf<ChatMessage>()
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString("username", "User")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        messageEditText = view.findViewById(R.id.message_edit_text)
        sendButton = view.findViewById(R.id.send_button)
        usernameTextView = view.findViewById(R.id.username_text_view)

        usernameTextView.text = username

        setupRecyclerView()

        // Add welcome message from bot
        addBotMessage("Hi $username! I'm Llama 2, how can I help you today?")

        sendButton.setOnClickListener {
            val message = messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                addUserMessage(message)
                messageEditText.text.clear()
                getBotResponse(message)
            }
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messagesList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
                reverseLayout = false
            }
            adapter = chatAdapter
        }
    }

    private fun addUserMessage(message: String) {
        val timestamp = getCurrentTime()
        messagesList.add(ChatMessage(message, timestamp, true))
        chatAdapter.notifyItemInserted(messagesList.size - 1)
        recyclerView.smoothScrollToPosition(messagesList.size - 1)
    }

    private fun addBotMessage(message: String) {
        val timestamp = getCurrentTime()
        messagesList.add(ChatMessage(message, timestamp, false))
        chatAdapter.notifyItemInserted(messagesList.size - 1)
        recyclerView.smoothScrollToPosition(messagesList.size - 1)
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getBotResponse(userMessage: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.sendMessage(userMessage)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val botResponse = response.body() ?:
                        "Sorry, I couldn't understand that."
                        addBotMessage(botResponse)
                    } else {
                        addBotMessage("Error: ${response.code()} - ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // More specific error handling
                    val errorMsg = when (e) {
                        is java.net.ConnectException -> "Couldn't connect to server. Is it running?"
                        is java.net.SocketTimeoutException -> "Request timed out"
                        is java.net.UnknownHostException -> "Invalid server address"
                        else -> "Error: ${e.localizedMessage ?: "Unknown error"}"
                    }
                    addBotMessage(errorMsg)
                    Log.e("ChatFragment", "Network error", e)
                }
            }
        }
    }
}