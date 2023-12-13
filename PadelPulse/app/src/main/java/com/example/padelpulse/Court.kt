package com.example.padelpulse

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Exclude
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.coroutineContext

class Court {
    lateinit var name: String
    lateinit var booked_timeslots: List<TimeSlot>

    constructor(name: String, booked_timeslots: List<TimeSlot>) {
        this.name = name
        this.booked_timeslots = booked_timeslots
    }

    constructor()


    fun isTimeslotAvailable(startTime: String, date: String): Boolean {
        val startTimeToBook = LocalTime.parse(startTime)
        val endTimeToBook = startTimeToBook.plusMinutes(90)
        val dateToBook = LocalDate.parse(date)

        for (slot in booked_timeslots) {
            if (slot.date == dateToBook ){
                if ((startTimeToBook.isAfter(slot.startTime) && startTimeToBook.isBefore(slot.endTime)) ||
                    (endTimeToBook.isAfter(slot.startTime) && endTimeToBook.isBefore(slot.endTime)) ||
                    (startTimeToBook.isBefore(slot.startTime) && endTimeToBook.isAfter(slot.endTime))) {
                    return false // Timeslot is not available
                }
            }
        }
        return true // Timeslot is available
    }

    fun bookTimeSlot(startTime: String, date: String):Boolean {
        if (isTimeslotAvailable(startTime, date)) {
            val slot = TimeSlot()
            slot.startTime = LocalTime.parse(startTime)
            slot.date = LocalDate.parse(date)
            slot.endTime = slot.startTime.plusMinutes(90)
            slot.players += Firebase.auth.currentUser?.uid.toString()
            booked_timeslots += slot

            val database = Firebase.database
            val courtsRef = database.getReference("Courts")
            val courtRef = courtsRef.child(name)
            courtRef.child("booked_timeslots").setValue(booked_timeslots)
            return true
        } else {
            return false
        }
    }

    fun showBookedTimeslots(): String {

        // Fetch the latest timeslots from the Firebase database
        val database = Firebase.database.getReference("Courts")
        val courtRef = database.child(name)
        courtRef.child("booked_timeslots").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                booked_timeslots = createTimeslotFromSnapshot(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadTimeslots:onCancelled", databaseError.toException())
            }
        })

        var result = "Already booked timeslots: \n"
        if (booked_timeslots.isEmpty()) {
            result += "None"
            return result
        }
        for (slot in booked_timeslots) {
            var sT = slot.startTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
            var eT = slot.endTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
            var d = slot.date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            result += "$d | $sT - $eT\n"
        }
        return result
    }

    fun createCourtFromSnapshot(snapshot: DataSnapshot): Court {
        name = snapshot.key.toString()
        booked_timeslots = createTimeslotFromSnapshot(snapshot.child("booked_timeslots"))
        Log.d(TAG, "Creating Court | Name: $name, Booked timeslots: $booked_timeslots")
        return Court(name, booked_timeslots)
    }

    private fun createTimeslotFromSnapshot(snapshot: DataSnapshot): List<TimeSlot> {
        val timeslots: MutableList<TimeSlot> = mutableListOf()
        for (timeslotSnapshot in snapshot.children) {
            val startTime = timeslotSnapshot.child("startTimeString").value
            val endTime = timeslotSnapshot.child("endTimeString").value
            val date = timeslotSnapshot.child("dateString").value
            val players: MutableList<String> = mutableListOf()
            players += timeslotSnapshot.child("players").value.toString()
            val timeSlot = TimeSlot(startTime.toString(), date.toString(), players)
            timeslots += timeSlot
        }
        return timeslots
    }

    class TimeSlot() {
        var startTimeString: String =
            LocalTime.of(1, 1, 0, 0).format(DateTimeFormatter.ISO_LOCAL_TIME)
        var endTimeString: String =
            LocalTime.of(1, 1, 0, 0).format(DateTimeFormatter.ISO_LOCAL_TIME)
        var dateString: String = LocalDate.of(2021, 1, 1).format(DateTimeFormatter.ISO_LOCAL_DATE)

        var players: List<String> = listOf()


        constructor(startTime: String, date: String, players:List<String>) : this() {
            this.startTimeString = startTime
            this.dateString = date
            this.endTimeString = LocalTime.parse(startTime).plusMinutes(90).format(DateTimeFormatter.ISO_LOCAL_TIME)
            this.players = players
        }

        override fun toString(): String {
            return "TimeSlot(startTimeString='$startTimeString', endTimeString='$endTimeString', date='$dateString')"
        }

        @get:Exclude
        @set:Exclude
        var startTime: LocalTime
            get() = LocalTime.parse(startTimeString)
            set(value) {
                startTimeString = value.format(DateTimeFormatter.ISO_LOCAL_TIME)
            }

        @get:Exclude
        @set:Exclude
        var endTime: LocalTime
            get() = LocalTime.parse(endTimeString)
            set(value) {
                endTimeString = value.format(DateTimeFormatter.ISO_LOCAL_TIME)
            }

        @get:Exclude
        @set:Exclude
        var date: LocalDate
            get() = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
            set(value) {
                dateString = value.format(DateTimeFormatter.ISO_LOCAL_DATE)
            }
    }
}