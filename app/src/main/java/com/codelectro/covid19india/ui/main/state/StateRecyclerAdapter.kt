package com.codelectro.covid19india.ui.main.state

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codelectro.covid19india.R
import com.codelectro.covid19india.models.State
import kotlinx.android.synthetic.main.state_item.view.*

class StateRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        private const val TAG = "StateRecyclerAdapter"
    }

    private var list: List<State> = ArrayList<State>();

    private lateinit var listener: ClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.state_item, parent, false)
        return StateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setStateList(list: List<State>){
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

        fun bind(state: State) {

            itemView.apply {
                stateName.text = state.state
                confirmedCase.text = state.confirmed.toString()
                activeCase.text = state.active.toString()
                recoveredCase.text = state.recovered.toString()
                deathCase.text = state.deaths.toString()
            }

        }
    }


    interface ClickListener {
        fun onClick(view: View, state: State)
    }
}
