package com.example.kitweathermap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kitweathermap.databinding.LayoutTextBinding

class WeatherResultAdapter : ListAdapter<Pair<String,String?>, RecyclerView.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LayoutTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    class ItemViewHolder(private val binding: LayoutTextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:Pair<String,String?>) {
            binding.tvKey.text = item.first
            binding.tvValue.text = item.second
        }
    }
}

class DiffCallback: DiffUtil.ItemCallback<Pair<String,String?>>() {
    override fun areItemsTheSame(oldItem: Pair<String,String?>, newItem: Pair<String,String?>): Boolean {
        return oldItem.second == newItem.second
    }

    override fun areContentsTheSame(oldItem: Pair<String,String?>, newItem: Pair<String,String?>): Boolean {
        return oldItem.second == newItem.second
    }
}
