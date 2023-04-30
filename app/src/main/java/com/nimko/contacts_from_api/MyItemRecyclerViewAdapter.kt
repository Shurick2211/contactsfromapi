package com.nimko.contacts_from_api

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nimko.contacts_from_api.databinding.FragmentItemBinding
import com.nimko.contacts_from_api.model.ItemForAdapter


class MyItemRecyclerViewAdapter: RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {
    var  values: MutableList<ItemForAdapter.Person> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values[position])
    }

    fun addNewPerson(person:ItemForAdapter.Person){
        values.add(person)
        refresh()
    }
    fun addAllPersons(persons:Collection<ItemForAdapter.Person>){
        values.clear()
        values.addAll(persons)
        refresh()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(){
        values.sortBy { it.firstName }
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemForAdapter.Person){
            val click = AdapterClickListener(this.itemView.context)
            binding.apply {
                firstName.text = item.firstName
                lastName.text = item.lastName
                itemView.setOnClickListener { click.onClick(item) }
                binding.callButton.setOnClickListener{ click.onClickCall(item) }
                binding.emailButton.setOnClickListener{ click.onClickEmail(item)}
            }
        }
    }

    interface Clickable{
        fun onClick(item: ItemForAdapter.Person)
        fun onClickCall(item: ItemForAdapter.Person)
        fun onClickEmail(item: ItemForAdapter.Person)
    }


}