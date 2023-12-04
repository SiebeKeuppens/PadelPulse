package com.example.padelpulse

import org.checkerframework.checker.units.qual.s
import java.time.LocalDateTime
import java.util.Date

class Court {
    lateinit var name: String
    lateinit var booked_timeslots: List<TimeSlot>

    constructor(name: String, booked_timeslots: List<TimeSlot>) {
        this.name = name
        this.booked_timeslots = booked_timeslots
    }

    constructor()

    fun bookTimeSlot(startTime: String) {
        val slot = TimeSlot(LocalDateTime.parse(startTime))
        booked_timeslots += slot
    }
}

class TimeSlot {
    var startTime: LocalDateTime
    var endTime: LocalDateTime

    constructor(startTime: LocalDateTime){
        this.startTime = startTime
        this.endTime = startTime.plusMinutes(90)
    }
}