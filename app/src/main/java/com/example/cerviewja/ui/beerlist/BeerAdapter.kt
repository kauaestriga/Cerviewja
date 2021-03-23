package com.example.cerviewja.ui.beerlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cerviewja.R
import com.example.cerviewja.domain.entity.Description

class BeerAdapter(
    private var beers: ArrayList<Description>
) : RecyclerView.Adapter<BeerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_beers_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beers[position])
    }

    override fun getItemCount(): Int {
        return beers.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(beerItem: Description) {
            val title =itemView.findViewById<TextView>(R.id.adp_title_beer)
            val price =itemView.findViewById<TextView>(R.id.adp_price)
            val alcoholContent =itemView.findViewById<TextView>(R.id.adp_alcohol_content)

            title.text = beerItem.nome
            price.text = "R$ ${beerItem.preco}"
            alcoholContent.text = "${beerItem.teorAlcoolico}%"
        }
    }

    fun addBeer(description: Description) {
        beers.add(description)
        notifyDataSetChanged()
    }

    fun removeBeer(position: Int) {
        beers.removeAt(position)
        notifyDataSetChanged()
    }
}