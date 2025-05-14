package com.example.chatbot_ai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginFragment : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameEditText = view.findViewById(R.id.username_edit_text)
        loginButton = view.findViewById(R.id.go_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()

            if (username.isEmpty()) {
                Toast.makeText(context, "Please enter a username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Navigate to chat fragment
            (activity as MainActivity).navigateToChatFragment(username)
        }
    }
}