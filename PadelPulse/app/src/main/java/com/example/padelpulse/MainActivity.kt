package com.example.padelpulse
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.padelpulse.databinding.ActivityMainBinding
import com.google.android.play.integrity.internal.t
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        auth = Firebase.auth
        setContentView(view)

        binding.RegisterButton.setOnClickListener(View.OnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        })
        binding.LoginButton.setOnClickListener(View.OnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)})
    }
}