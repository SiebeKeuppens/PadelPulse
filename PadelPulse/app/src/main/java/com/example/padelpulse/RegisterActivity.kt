package com.example.padelpulse

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.padelpulse.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        auth = Firebase.auth
        setContentView(view)

        binding.RegisterBackButton.setOnClickListener(View.OnClickListener {
            finish()
        })

        binding.RegisterSubmitButton.setOnClickListener(View.OnClickListener {
            val username = binding.RTextName.text.toString()
            val email = binding.RTextEmail.text.toString()
            val password = binding.RTextPassword.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "Please fill out all fields.",
                    Toast.LENGTH_SHORT,
                ).show()
                return@OnClickListener
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser!!
                            val profileUpdate = UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build()
                            user.updateProfile(profileUpdate)
                            Toast.makeText(
                                baseContext,
                                "Authentication success!",
                                Toast.LENGTH_LONG,
                            ).show()
                            //createNewUser(FirebaseAuth.getInstance().currentUser!!)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        })

    }
}