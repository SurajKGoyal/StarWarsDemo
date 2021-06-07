package com.heliushouse.starwars.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.heliushouse.starwars.R
import com.heliushouse.starwars.databinding.ListItemNameBinding
import com.heliushouse.starwars.model.People

class PeopleAdapter(private val list: List<People>):  RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_name,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.people = list[position]
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(val binding: ListItemNameBinding) :
        RecyclerView.ViewHolder(binding.root)
}