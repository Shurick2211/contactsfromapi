package com.nimko.contacts_from_api

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nimko.contacts_from_api.databinding.FragmentItemBinding
import com.nimko.contacts_from_api.model.Person


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    val click:Clickable
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

   val values: MutableList<Person> = listOf(
        Person("Ivan","Ivanov","+123456789","qwer@ty","fff",null),
        Person("Petr","Petrenko","+89533449","ert@ty","fff",null),
        Person("Stepa","Stepanenko","+4525429","errrrtr@ty","fff",null),
        Person("Sophy","Popovich","+72727245","yth@ty","fff",null),
        Person("Mary","Lavis","+24524452425","drgr@ty","fff",null)
    ) as MutableList<Person>

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
}