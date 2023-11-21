package com.example.padelpulse

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.padelpulse.databinding.ActivityRegisterBinding

import com.example.padelpulse.databinding.ActivityUserBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        val view = binding.root
        auth = Firebase.auth
        setContentView(view)

        binding.UserBackButton.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.UsernameText.text = currentUser.email
        } //Else, redirect to login page
        else {
            finish()
        }

    }
}