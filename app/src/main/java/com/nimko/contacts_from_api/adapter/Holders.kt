package com.nimko.contacts_from_api.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.nimko.contacts_from_api.databinding.FragmentHeaderBinding
import com.nimko.contacts_from_api.databinding.FragmentItemBinding
import com.nimko.contacts_from_api.model.ItemForAdapter

abstract class ItemsHolder(binding: ViewBinding,val click:ClickItem) : RecyclerView.ViewHolder(binding.root){
    abstract fun bind(item: ItemForAdapter)
}

class PersonViewHolder(val binding: FragmentItemBinding, click: ClickItem) : ItemsHolder(binding,
    click
)  {
    override fun bind(item: ItemForAdapter){
        item as ItemForAdapter.Person
        val clickEmailOrCall = AdapterClickCallOrEmail(this.itemView.context)
        binding.apply {
            firstName.text = item.firstName
            lastName.text = item.lastName
            itemView.setOnClickListener { click.click(item.id!!) }
            binding.callButton.setOnClickListener { clickEmailOrCall.onClickCall(item) }
            binding.emailButton.setOnClickListener { clickEmailOrCall.onClickEmail(item) }
        }
    }

}


class HeaderViewHolder(val binding: FragmentHeaderBinding, click: ClickItem) : ItemsHolder(binding,
    click
) {
    override fun bind(item: ItemForAdapter){
        item as ItemForAdapter.Header
        binding.apply {
            title.text = item.title
            if(item.progress) progressBar.visibility = View.VISIBLE
            else progressBar.visibility = View.GONE
        }
    }


}