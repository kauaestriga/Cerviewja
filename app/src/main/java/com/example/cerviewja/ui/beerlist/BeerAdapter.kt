package com.example.cerviewja.ui.beerlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cerviewja.R
import com.example.cerviewja.domain.entity.Description

class BeerAdapter(
    private var beers: ArrayList<Description>,
    private var myClickListener: MyClickListener
) : RecyclerView.Adapter<BeerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_beers_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(beers[position], myClickListener)
    }

    override fun getItemCount(): Int {
        return beers.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(beerItem: Description, myClickListener: MyClickListener) {
            val tvTitle = itemView.findViewById<TextView>(R.id.adp_title_beer)
            val tvPrice = itemView.findViewById<TextView>(R.id.adp_price)
            val tvAlcoholContent =itemView.findViewById<TextView>(R.id.adp_alcohol_content)
            val ivShare = itemView.findViewById<ImageView>(R.id.iv_beer_share)
            val ivCall = itemView.findViewById<ImageView>(R.id.iv_beer_phone)

            tvTitle.text = beerItem.nome
            tvPrice.text = "R$ ${beerItem.preco}"
            tvAlcoholContent.text = "${beerItem.teorAlcoolico}%"

            itemView.setOnClickListener { myClickListener.onClick(beerItem) }
            itemView.setOnLongClickListener {
                myClickListener.onLongClick(beerItem)
                return@setOnLongClickListener true
            }

            ivShare.setOnClickListener {
                myClickListener.onShareClick(beerItem)
            }

            ivCall.setOnClickListener {
                myClickListener.onCallClick()
            }
        }
    }

    fun addBeer(description: Description) {
        beers.add(description)
        notifyDataSetChanged()
    }

    fun removeBeer(description: Description) {
        beers.remove(description)
        notifyDataSetChanged()
    }
}