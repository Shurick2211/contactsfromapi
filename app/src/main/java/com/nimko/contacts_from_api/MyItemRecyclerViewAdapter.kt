package com.nimko.contacts_from_api

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nimko.contacts_from_api.databinding.FragmentItemBinding
import com.nimko.contacts_from_api.model.Person


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    val click:Clickable
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>(),Requestable {

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
    }

    fun addNewPerson(person:Person){
        values.add(person)
        refresh()
    }

    fun refresh(){
        this.notifyDataSetChanged()
    }


    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val fN: TextView = binding.firstName
        val lN: TextView = binding.lastName

        override fun toString(): String {
            return super.toString() + " '${fN.text} ${lN.text}'"
        }
    }

    interface Clickable{
        fun onClick(item: Person)
    }

    override fun getRequest(request: String) {
        val sType = object : TypeToken<List<Person>>() { }.type
        val persons = Gson().fromJson<List<Person>>(request, sType)
        Log.d("LIST", persons.toString())
        values.addAll(persons)
    }
}