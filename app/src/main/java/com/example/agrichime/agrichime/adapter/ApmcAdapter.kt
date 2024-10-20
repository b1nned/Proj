package com.example.agrichime.agrichime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agrichime.R
import com.example.agrichime.agrichime.model.data.APMCCustomRecords

class ApmcAdapter(val context: Context, val data: List<APMCCustomRecords>) :
    RecyclerView.Adapter<ApmcAdapter.ApmcViewHolder>() {
    class ApmcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val market: TextView = itemView.findViewById(R.id.apmcNameValue)
        val location: TextView = itemView.findViewById(R.id.apmcLocationValue)
        val commodity: TextView = itemView.findViewById(R.id.comodityname)
        val min: TextView = itemView.findViewById(R.id.minvalue)
        val max: TextView = itemView.findViewById(R.id.maxvalue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApmcViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.apmc_single_list, parent, false)
        return ApmcViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ApmcViewHolder, position: Int) {
        val mainData = data[position]
        with(holder) {
            market.text = mainData.market
            location.text = "${mainData.district}, ${mainData.state}"
            commodity.text = mainData.commodity.joinToString("\n")
            min.text = mainData.min_price.joinToString("\n")
            max.text = mainData.max_price.joinToString("\n")
        }
    }
}