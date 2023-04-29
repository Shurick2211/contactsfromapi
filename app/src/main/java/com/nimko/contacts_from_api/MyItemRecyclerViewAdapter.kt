package com.nimko.contacts_from_api

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nimko.contacts_from_api.databinding.FragmentItemBinding
import com.nimko.contacts_from_api.model.Person


class MyItemRecyclerViewAdapter(
    val click:Clickable
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

   var  values: MutableList<Person> = ArrayList()

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
        val item = values[position]
        holder.lN.text = item.lastName
        holder.fN.text = item.firstName
        holder.itemView.setOnClickListener {
            click.onClick(item)
        }
        holder.call.setOnClickListener {
           click.onClickCall(item)
        }
        holder.email.setOnClickListener {
           click.onClickEmail(item)
        }
    }

    fun addNewPerson(person:Person){
        values.add(person)
        refresh()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(){
        values.sortBy { it.firstName }
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val fN = binding.firstName
        val lN = binding.lastName
        val call = binding.callButton
        val email = binding.emailButton
    }

    interface Clickable{
        fun onClick(item: Person)
        fun onClickCall(item: Person)
        fun onClickEmail(item: Person)
    }


}