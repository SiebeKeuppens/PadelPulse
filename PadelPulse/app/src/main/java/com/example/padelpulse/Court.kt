package com.example.padelpulse

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Court {
    lateinit var name: String
    lateinit var booked_timeslots: List<TimeSlot>

    constructor(name: String, booked_timeslots: List<TimeSlot>) {
        this.name = name
        this.booked_timeslots = booked_timeslots
    }

    constructor()

    fun bookTimeSlot(startTime: String) {
        val slot = TimeSlot(startTime)
        slot.startTimeString = startTime
        booked_timeslots += slot
    }

    fun showBookedTimeslots(): String {
        var result = "Already booked timeslots: \n"
        for (slot in booked_timeslots) {
            var sT = slot.startTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
            var eT = slot.endTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
            result += "$sT - $eT\n"
        }
        return result
    }

    fun createCourtFromSnapshot(snapshot: DataSnapshot): Court {
        name = snapshot.child("name").value.toString()
        booked_timeslots = createTimeslotFromSnapshot(snapshot.child("booked_timeslots"))

        Log.d(TAG, "Creating Court | Name: $name, Booked timeslots: $booked_timeslots")
        return Court(name, booked_timeslots!!)
    }

    private fun createTimeslotFromSnapshot(snapshot: DataSnapshot): List<TimeSlot> {
        var timeslots: List<TimeSlot> = listOf()
        for (timeslotSnapshot in snapshot.children) {
            val startTimeSnapshot = timeslotSnapshot.child("startTime")

            val hour = startTimeSnapshot.child("hour").getValue() as Long
            val minute = startTimeSnapshot.child("minute").getValue() as Long
            val day = startTimeSnapshot.child("dayOfMonth").getValue() as Long
            val month = startTimeSnapshot.child("monthValue").getValue() as Long
            val year = startTimeSnapshot.child("year").getValue() as Long
            val startTime = LocalDateTime.of(LocalDate.of((year).toInt(),(month).toInt(),(day).toInt()), LocalTime.of((hour).toInt(), (minute).toInt()))
            val timeSlot = TimeSlot(startTime.toString())
            timeslots += timeSlot

            Log.d(TAG, "Creating Timeslot | Hour: $hour, Minute: $minute Day: $day, Month: $month")
            Log.d(TAG, "Creating Timeslot | startTime: $startTime")
            Log.d(TAG, "Creating Timeslot | timeSlot: ${timeSlot.toString()}")


        }
        Log.d(TAG, "Creating Timeslot| timeSlots: ${timeslots}")
        return timeslots
    }
}

class TimeSlot() {
    var startTimeString: String = LocalDateTime.of(2021, 1, 1, 0, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    var endTimeString: String = LocalDateTime.of(2021, 1, 1, 0, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    constructor(startTime: String) : this() {
        this.startTimeString = startTime
        this.endTimeString = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME).plusHours(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    override fun toString(): String {
        return "TimeSlot(startTimeString='$startTimeString', endTimeString='$endTimeString')"
    }

    @get:Exclude
    @set:Exclude
    var startTime: LocalDateTime
        get() = LocalDateTime.parse(startTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        set(value) {
            startTimeString = value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }

    @get:Exclude
    @set:Exclude
    var endTime: LocalDateTime
        get() = LocalDateTime.parse(endTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        set(value) {
            endTimeString = value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
}