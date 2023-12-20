package com.example.padelpulse

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalTime

class MatchAdapter (private val mMatches:List<Court.IndexedTimeSlot>) : RecyclerView.Adapter<MatchAdapter.ViewHolder>() {
    private lateinit var auth: FirebaseAuth
    private val timeslotIndices = mutableMapOf<Court.TimeSlot, Int>()


    init {
        // Populate the timeslotIndices map
        for ((index, match) in mMatches.withIndex()) {
            timeslotIndices[match.timeSlot] = index
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val joinButton = itemView.findViewById<Button>(R.id.Join_button)
        val detailsTextview = itemView.findViewById<TextView>(R.id.MatchDetails)

        init {
            auth = Firebase.auth
            joinButton.setOnClickListener(View.OnClickListener {
                val position: Int = adapterPosition
                val match: Court.IndexedTimeSlot = mMatches[position]
                val timeSlot = match.timeSlot
                val courtName = match.courtName
                val timeslotIndex = match.timeslotIndex
                timeSlot.players += auth.currentUser?.displayName ?: "Anonymous"
                // Update the players field in the database
                val playersRef = Firebase.database.reference.child("Courts").child(courtName).child("booked_timeslots").child(timeslotIndex.toString()).child("players")
                playersRef.setValue(timeSlot.players)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val matchView = inflater.inflate(R.layout.item_match, parent, false)
        return ViewHolder(matchView)
    }

    override fun onBindViewHolder(viewHolder: MatchAdapter.ViewHolder, position: Int) {
        val match: Court.IndexedTimeSlot = mMatches[position]
        val timeSlot = match.timeSlot
        val details = viewHolder.detailsTextview
        val players = timeSlot.players.joinToString(separator = ", ")
        val matchText = StringBuilder("Players: $players")

        // Add null checks before parsing startTime and date
        if (timeSlot.startTimeString != null) {
            val startTime = LocalTime.parse(timeSlot.startTimeString)
            matchText.append("\nStart Time: $startTime")
        }
        if (timeSlot.dateString != null) {
            val date = LocalDate.parse(timeSlot.dateString)
            matchText.append("\nDate: $date")
        }

        details.text = matchText.toString()
    }

    override fun getItemCount(): Int {
        return mMatches.size
    }

}