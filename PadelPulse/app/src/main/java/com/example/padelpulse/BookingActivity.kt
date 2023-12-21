package com.example.padelpulse

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.padelpulse.databinding.ActivityBookingBinding
import com.google.android.play.integrity.internal.c
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

//TODO: Check out the error that happens when selecting a time that has only 1 digit in the minutes (12:9)
//TODO: Make it so that you can't book a timeslot that is already booked

class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding
    private lateinit var auth: FirebaseAuth

    private var time : LocalTime = LocalTime.now()
    private var date : LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        val view = binding.root
        auth = Firebase.auth
        setContentView(view)

        val courtName = intent.getStringExtra("courtName")
        binding.courtName.text = courtName

        binding.BookBackButton.setOnClickListener { finish() }

        binding.TimePickerButton.setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(supportFragmentManager, "timePicker")
        }

        binding.DatePickerButton.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(supportFragmentManager, "datePicker")
        }

        binding.BookingSubmitButton.setOnClickListener {
            getCourtByName(courtName) { court ->
                if (court != null) {
                    if(court.bookTimeSlot(time.toString(), date.toString())) {
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Timeslot is booked. Try Again!", Toast.LENGTH_SHORT).show()
                    }
                    finish()
                } else {
                    finish()
                }
            }
        }
    }

    private fun getCourtByName(courtName: String?, callback: (Court?) -> Unit) {
        val courts = mutableListOf<Court>()
        val database = Firebase.database.getReference("Courts")

        val courtListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (courtSnapshot in dataSnapshot.children) {
                    val courtToAdd = Court()
                    courtToAdd.createCourtFromSnapshot(courtSnapshot)
                    courts.add(courtToAdd)
                }
                callback(courts.find { it.name == courtName })
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        database.addListenerForSingleValueEvent(courtListener)
    }

    fun setBookingTime (timeToSet: LocalTime){
        time = timeToSet
        binding.BookingTimeText.text = time.toString()
    }

    fun setBookingDate (dateToSet: LocalDate) {
        date = dateToSet
        binding.BookingDateText.text = date.toString()
    }
}