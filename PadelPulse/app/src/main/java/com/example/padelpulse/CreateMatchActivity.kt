package com.example.padelpulse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.padelpulse.databinding.ActivityCreatematchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreateMatchActivity: AppCompatActivity() {
    private lateinit var binding: ActivityCreatematchBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatematchBinding.inflate(layoutInflater)
        val view = binding.root
        auth = Firebase.auth
        val username = auth.currentUser?.displayName.toString()
        binding.Player1Name.setText(username)
        setContentView(view)

        binding.CreateMatchBackButton.setOnClickListener {
            finish()
        }

        binding.BookingButton.setOnClickListener{
            intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("Players", binding.Player1Name.text.toString() + ", " + binding.Player2Name.text.toString() + ", " + binding.Player3Name.text.toString() + ", " + binding.Player4Name.text.toString())
            startActivity(intent)
        }
    }
}