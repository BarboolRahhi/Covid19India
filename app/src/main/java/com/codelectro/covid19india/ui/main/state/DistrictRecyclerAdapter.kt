package com.codelectro.covid19india.ui.main.state

import android.content.res.ColorStateList
import android.graphics.Color
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codelectro.covid19india.R
import com.codelectro.covid19india.entity.District
import com.codelectro.covid19india.ui.formatNumber
import kotlinx.android.synthetic.main.district_item.view.*


class DistrictRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<District> = ArrayList<District>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.district_item, parent, false)
        return StateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setStateList(list: List<District>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StateViewHolder).bind(list[position])
    }

    class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(district: District) {
            itemView.stateName.text = district.name
            itemView.confirmedCase.text = district.confirmed.formatNumber()
            itemView.recoveredCase.text = district.recovered.formatNumber()
            itemView.deathCase.text = district.deceased.formatNumber()
            itemView.activeCase.text = district.active.formatNumber()
        }
    }
}