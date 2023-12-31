package com.example.padelpulse

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.padelpulse.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//TODO: Add a way to view open matches
//TODO: Add a way to join a match
//TODO: Maybe flesh out the profile page a bit more

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
            auth.signOut();
            finish()
        })

        binding.UserEditButton.setOnClickListener(View.OnClickListener {
            intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        })

        binding.MatchButton.setOnClickListener(View.OnClickListener {
            intent = Intent(this, MatchActivity::class.java)
            startActivity(intent)
        })
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.UsernameText.text = currentUser.displayName
        }
        else {
            finish()
        }
    }
}