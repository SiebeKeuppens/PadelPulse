package com.example.padelpulse

import android.app.Activity
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.padelpulse.databinding.ActivityEditprofileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditprofileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        val view = binding.root
        auth = Firebase.auth
        setContentView(view)

        binding.EditBackButton.setOnClickListener {
            finish()
        }

        binding.EditSubmitButton.setOnClickListener {

            val user = Firebase.auth.currentUser
            val profileUpdates = userProfileChangeRequest {
                displayName = binding.ETextName.text.toString()
            }
            val credentials = EmailAuthProvider.getCredential(
                user!!.email.toString(),
                binding.ETextOldPassword.text.toString()
            )

            if (binding.ETextOldPassword.text.toString().isEmpty()) {
                Toast.makeText(
                    baseContext,
                    "Please enter your old password.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            user!!.reauthenticate(credentials)
                .addOnCompleteListener {
                    user.updateProfile(profileUpdates)

                    /*if (user.email != binding.ETextEmail.text.toString()) {
                        Log.d(TAG, "Email: ${user.email}")
                        user.verifyBeforeUpdateEmail(binding.ETextEmail.text.toString())
                        Log.d(TAG, "New? Email: ${user.email}")
                    }*/


                    if (binding.ETextPassword.text.toString().isNotEmpty() && binding.ETextPassword.text.toString() != binding.ETextOldPassword.text.toString()) {
                        user.updatePassword(binding.ETextPassword.text.toString())
                    }

                    Toast.makeText(baseContext, "Change Successful", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }

    public override fun onStart() {
        super.onStart()
        //Fill in the email and username fields
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //binding.ETextEmail.setText(currentUser.email)
            binding.ETextName.setText(currentUser.displayName)
        }
    }
}