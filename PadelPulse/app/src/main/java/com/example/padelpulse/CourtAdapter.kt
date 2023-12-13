package com.example.padelpulse

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class CourtAdapter (private val mCourts:List<Court>) : RecyclerView.Adapter<CourtAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.court_name)
        val bookButton = itemView.findViewById<Button>(R.id.book_button)
        val detailsTextview = itemView.findViewById<TextView>(R.id.CourtDetails)

        init {
            bookButton.setOnClickListener(View.OnClickListener {
                val intent = Intent(itemView.context, BookingActivity::class.java)
                intent.putExtra("courtName", nameTextView.text)
                startActivity(itemView.context, intent, null)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourtAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val courtView = inflater.inflate(R.layout.item_court, parent, false)
        return ViewHolder(courtView)
    }

    override fun onBindViewHolder(viewHolder: CourtAdapter.ViewHolder, position: Int) {
        val court: Court = mCourts.get(position)
        val textView = viewHolder.nameTextView
        textView.setText(court.name)
        val details = viewHolder.detailsTextview
        details.text = court.showBookedTimeslots()
    }

    override fun getItemCount(): Int {
        return mCourts.size
    }
}