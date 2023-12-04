package com.example.padelpulse

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CourtsFragment : Fragment(R.layout.fragment_courts) {
    private lateinit var database: DatabaseReference
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_courts, container, false)

        database = Firebase.database.reference

        val courtLayout: LinearLayout = view.findViewById(R.id.court_list)
        val textView = TextView(this.context)
        textView.text = "I am added dynamically to the view"


        var courtname: String?
        val courtNameView = TextView(this.context)


        val courtListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val court = dataSnapshot.child("Courts")
                for (courtSnapshot in court.children) {
                    var courtname = courtSnapshot.child("name").getValue<String>()
                    Log.d(TAG, "Court name: $courtname")
                    courtNameView.text = courtname
                    courtLayout.addView(courtNameView)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadCourt:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(courtListener)
        return view
    }
}