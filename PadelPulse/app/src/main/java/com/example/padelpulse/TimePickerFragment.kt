package com.example.padelpulse

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalTime
import java.util.Calendar

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val formattedHour = String.format("%02d", hourOfDay)
        val formattedMinute = String.format("%02d", minute)
        var time = LocalTime.parse("$formattedHour:$formattedMinute")
        val bookingActivity = activity as BookingActivity
        bookingActivity.setBookingTime(time)
    }
}
