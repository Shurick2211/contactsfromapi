package com.nimko.contacts_from_api

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.nimko.contacts_from_api.databinding.FragmentHeaderBinding
import com.nimko.contacts_from_api.databinding.FragmentItemBinding
import com.nimko.contacts_from_api.model.ItemForAdapter

abstract class ItemsHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root){
    abstract fun bind(item: Any)
}

class PersonViewHolder(val binding: FragmentItemBinding) : ItemsHolder(binding)  {
    override fun bind(item: Any){
        item as ItemForAdapter.Person
        val click = AdapterClickListener(this.itemView.context)
        binding.apply {
            firstName.text = item.firstName
            lastName.text = item.lastName
            itemView.setOnClickListener { click.onClick(item) }
            binding.callButton.setOnClickListener { click.onClickCall(item) }
            binding.emailButton.setOnClickListener { click.onClickEmail(item) }
        }
    }

}


class HeaderViewHolder(val binding: FragmentHeaderBinding) : ItemsHolder(binding) {
    override fun bind(item: Any){
        item as ItemForAdapter.Header
        binding.apply {
            title.text = item.title
        }
    }


}