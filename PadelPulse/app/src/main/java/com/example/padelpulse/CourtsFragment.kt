package com.example.padelpulse

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class CourtsFragment : Fragment(R.layout.fragment_courts) {
    private lateinit var database: DatabaseReference
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_courts, container, false)

        database = Firebase.database.reference

        val rvCourts: RecyclerView = view.findViewById(R.id.rvCourts)
        //val details: TextView = rvCourts.findViewById(R.id.CourtDetails)
        val courts = mutableListOf<Court>()
        val courtAdapter = CourtAdapter(courts)
        rvCourts.adapter = courtAdapter
        rvCourts.layoutManager = LinearLayoutManager(this.context)

        val courtListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val court = dataSnapshot.child("Courts")
                for (courtSnapshot in court.children) {
                   val courtToAdd = Court()
                    courtToAdd.createCourtFromSnapshot(courtSnapshot)
                    Log.d(TAG, "Created Court | Courtname: ${courtToAdd.name}, Booked timeslots: ${courtToAdd.booked_timeslots}")

                    //details.text = "Courtname: ${courtToAdd.name}, Booked timeslots: ${courtToAdd.booked_timeslots}"

                    courts.add(courtToAdd)

                }
                courtAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadCourt:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(courtListener)
        return view
    }
}