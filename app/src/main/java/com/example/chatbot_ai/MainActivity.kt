package com.example.chatbot_ai

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentContainer = findViewById(R.id.fragment_container)

        // Load the login fragment initially
        if (savedInstanceState == null) {
            loadFragment(LoginFragment())
        }
    }

    fun navigateToChatFragment(username: String) {
        val chatFragment = ChatFragment().apply {
            arguments = Bundle().apply {
                putString("username", username)
            }
        }
        loadFragment(chatFragment)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}