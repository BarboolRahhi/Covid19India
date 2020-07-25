package com.codelectro.covid19india.ui.main.state

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codelectro.covid19india.R
import com.codelectro.covid19india.entity.StateWise
import com.codelectro.covid19india.ui.dateTimeFormat
import com.codelectro.covid19india.ui.formatNumber
import kotlinx.android.synthetic.main.state_item.view.*

class StateRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        private const val TAG = "StateRecyclerAdapter"
    }

    private var list: List<StateWise> = ArrayList<StateWise>()

    private lateinit var listener: ClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.state_item, parent, false)
        return StateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setStateList(list: List<StateWise>){
        this.list = list
        notifyDataSetChanged()
    }


    fun setOnClickItemListener(listener: ClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        ( holder as StateViewHolder).bind(list[position])

        holder.itemView.setOnClickListener {
            Log.d(TAG, "onBindViewHolder: Adapter")
            listener?.onClick(it,  list[position])
        }
    }

    class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(state: StateWise) {
            itemView.apply {
                stateName.text = state.state
                confirmedCase.text = state.confirmed.formatNumber()
                activeCase.text = state.active.formatNumber()
                recoveredCase.text = state.recovered.formatNumber()
                deathCase.text = state.deaths.formatNumber()
                deltaConfirmed.text = "+"+state.deltaconfirmed.formatNumber()
                deltaRecovered.text = "+"+state.deltarecovered.formatNumber()
                deltaDeaths.text = "+"+state.deltadeaths.formatNumber()
                lastUpdateDate.text = "Last Update date: " + state.lastupdatedtime!!.dateTimeFormat()
            }

        }
    }


    interface ClickListener {
        fun onClick(view: View, state: StateWise)
    }
}
