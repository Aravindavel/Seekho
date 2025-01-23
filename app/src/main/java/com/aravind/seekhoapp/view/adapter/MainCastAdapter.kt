package com.aravind.seekhoapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aravind.seekhoapp.databinding.ItemGenreCastBinding
import com.aravind.seekhoapp.model.AnimeModel

class MainCastAdapter(
    val context: Context,
    var producers: ArrayList<AnimeModel.Producers>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGenreCastBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return producers.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(producers[position],position)
        }
    }

    inner class ViewHolder(private val binding: ItemGenreCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AnimeModel.Producers,position: Int) {
            binding.tvContent.text = item.name

        }
    }
}


