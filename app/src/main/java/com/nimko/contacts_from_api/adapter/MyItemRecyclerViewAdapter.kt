package com.nimko.contacts_from_api.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nimko.contacts_from_api.databinding.FragmentHeaderBinding
import com.nimko.contacts_from_api.databinding.FragmentItemBinding
import com.nimko.contacts_from_api.model.ItemForAdapter


class MyItemRecyclerViewAdapter: RecyclerView.Adapter<ItemsHolder>() {
    var values: MutableList<ItemForAdapter> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        when(values[position]){
            is ItemForAdapter.Person -> return ITEM
            is ItemForAdapter.Header -> return HEADER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsHolder {

        if (viewType == HEADER) return HeaderViewHolder(FragmentHeaderBinding
                .inflate(LayoutInflater.from(parent.context),parent,false))

        return PersonViewHolder(FragmentItemBinding
            .inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: ItemsHolder, position: Int) {
        holder.bind(values[position])
    }


    fun addNewPerson(person:ItemForAdapter){
        values.add(person)
        refresh()
    }
    fun addAllPersons(persons:Collection<ItemForAdapter>){
        values.clear()
        values.addAll(persons)
        refresh()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(){
      //  values.sortBy { it.firstName }
        this.notifyDataSetChanged()
    }
    override fun getItemCount(): Int = values.size

    companion object{
        const val ITEM = 0
        const val HEADER = 1
    }
}