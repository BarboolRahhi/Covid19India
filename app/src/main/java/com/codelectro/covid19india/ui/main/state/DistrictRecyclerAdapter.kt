package com.codelectro.covid19india.ui.main.state

import android.content.res.ColorStateList
import android.graphics.Color
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codelectro.covid19india.R
import com.codelectro.covid19india.models.District
import kotlinx.android.synthetic.main.district_item.view.*


class DistrictRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<District> = ArrayList<District>();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.district_item, parent, false)
        return StateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setStateList(list: List<District>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        ( holder as StateViewHolder).bind(list[position])
    }

    class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(district: District) {

            itemView.stateName.text = district.name

            if (district.confirmed == null)
                itemView.confirmedCase.text = "No Data"
            else
                itemView.confirmedCase.text = district.confirmed.toString()

            if (district.recovered == null)
                itemView.recoveredCase.text = "No Data"
            else
                itemView.recoveredCase.text = district.recovered.toString()

            if (district.deaths == null)
                itemView.deathCase.text = "No Data"
            else
                itemView.deathCase.text = district.deaths.toString()


            when(district.zone) {
                "RED" -> itemView.cardView.setCardBackgroundColor(Color.parseColor("#ff4d4d"))
                "GREEN" -> itemView.cardView.setCardBackgroundColor(Color.parseColor("#05c46b"))
                "ORANGE" -> itemView.cardView.setCardBackgroundColor(Color.parseColor("#FF5722"))
                else -> itemView.cardView.setCardBackgroundColor(Color.parseColor("#1530A5"))
            }

        }
    }
}