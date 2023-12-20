package com.example.padelpulse

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.padelpulse.databinding.ActivityMatchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    val courtCounters = HashMap<String, Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        val view = binding.root
        auth = Firebase.auth
        database = Firebase.database.reference

        val rvmatches = binding.rvmatches
        val matches = mutableListOf<Court.IndexedTimeSlot>()
        val matchAdapter = MatchAdapter(matches)
        rvmatches.adapter = matchAdapter
        rvmatches.layoutManager = LinearLayoutManager(baseContext)

        val matchListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val match = dataSnapshot.child("Courts")
                matches.clear()
                for (courtSnapshot in match.children) {
                    val court = Court()
                    court.createCourtFromSnapshot(courtSnapshot)
                    courtCounters[court.name] = 0 // Reset the counter for the current court
                    for (timeSlot in court.booked_timeslots) {
                        if (timeSlot.players.isNotEmpty()) {
                            val indexedTimeSlot = Court.IndexedTimeSlot(timeSlot, court.name, courtCounters[court.name]!!)
                            matches.add(indexedTimeSlot)
                            courtCounters[court.name] = courtCounters[court.name]!! + 1 // Increment the counter for the current court
                        }
                    }
                }
                matchAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadCourt:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(matchListener)
        setContentView(view)

        binding.MatchBackButton.setOnClickListener {
            finish()
        }

        binding.CreateMatchButton.setOnClickListener {
            intent = Intent(this, CreateMatchActivity::class.java)
            startActivity(intent)
        }
    }
}