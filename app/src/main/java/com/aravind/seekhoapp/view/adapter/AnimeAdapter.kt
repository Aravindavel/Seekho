package com.aravind.seekhoapp.view.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aravind.seekhoapp.R
import com.aravind.seekhoapp.databinding.ItemAnimeBinding
import com.aravind.seekhoapp.model.AnimeModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class AnimeAdapter(
    val context: Context,
    var model: ArrayList<AnimeModel.Data>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isLoading = false

    companion object {
        private const val ITEM_VIEW_TYPE = 1
        private const val LOADING_VIEW_TYPE = 2
    }

    lateinit var onAnimeClick: (position: Int) -> Unit

    override fun getItemViewType(position: Int): Int {
        return if (position < model.size) ITEM_VIEW_TYPE else LOADING_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM_VIEW_TYPE) {
            val binding = ItemAnimeBinding.inflate(inflater)
            ViewHolder(binding)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_loader, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return model.size + if (isLoading) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(model[position],position)
        }
    }

    inner class ViewHolder(private val binding: ItemAnimeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AnimeModel.Data,position: Int) {
            binding.tvTitle.text = data.title
            binding.tvRating.text = data.score.toString()
            binding.tvEpisodes.text = data.episodes.toString()
            Glide.with(binding.root.context).load(data.images.jpg.imageUrl).placeholder(R.drawable.ic_default)
                .error(R.drawable.demo_profile).into(binding.ivPostImage)
            binding.root.setOnClickListener { onAnimeClick(position) }
        }
    }

    inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun showLoading() {
        isLoading = true
        notifyItemInserted(model.size)
    }

    fun hideLoading() {
        isLoading = false
        notifyItemRemoved(model.size)
    }

}
