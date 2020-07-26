package com.codelectro.covid19india.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codelectro.covid19india.R
import com.codelectro.covid19india.entity.District
import com.codelectro.covid19india.ui.formatNumber
import kotlinx.android.synthetic.main.district_item.view.*
import kotlinx.android.synthetic.main.district_item.view.activeCase
import kotlinx.android.synthetic.main.district_item.view.confirmedCase
import kotlinx.android.synthetic.main.district_item.view.deathCase
import kotlinx.android.synthetic.main.district_item.view.recoveredCase
import kotlinx.android.synthetic.main.district_item.view.stateName



class DistrictRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<District> = ArrayList<District>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.district_item, parent, false)
        return StateViewHolder(
            view
        )
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
            itemView.apply {
                stateName.text = district.name
                confirmedCase.text = district.confirmed.formatNumber()
                recoveredCase.text = district.recovered.formatNumber()
                deathCase.text = district.deceased.formatNumber()
                activeCase.text = district.active.formatNumber()
                deltaConfirmed.text = "+"+district.deltaconfirmed.formatNumber()
                deltaRecovered.text = "+"+district.deltarecovered.formatNumber()
                deltaDeaths.text = "+"+district.deltadeaths.formatNumber()
            }


        }
    }
}