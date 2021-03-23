package com.example.cerviewja.ui.about

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cerviewja.R
import com.example.cerviewja.domain.entity.Developer

class AboutAdapter(
    private var devs: ArrayList<Developer>
) : RecyclerView.Adapter<AboutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_developer_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(devs[position])
    }

    override fun getItemCount(): Int {
        return devs.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(devItem: Developer) {
            val title = itemView.findViewById<TextView>(R.id.dev_name)
            val price = itemView.findViewById<TextView>(R.id.dev_rm)

            title.text = devItem.nome
            price.text = devItem.rm
        }
    }

    fun addDev(developer: Developer) {
        devs.add(developer)
        notifyDataSetChanged()
    }
}