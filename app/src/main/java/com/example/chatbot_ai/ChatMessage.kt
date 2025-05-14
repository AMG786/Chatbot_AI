package com.example.chatbot_ai

// Data class to represent a chat message
data class ChatMessage(
    val message: String,
    val timestamp: String,
    val isUser: Boolean
)