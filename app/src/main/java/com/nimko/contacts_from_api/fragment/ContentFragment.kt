package com.nimko.contacts_from_api.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nimko.contacts_from_api.R
import com.nimko.contacts_from_api.databinding.FragmentContentBinding
import com.nimko.contacts_from_api.model.ItemForAdapter

class ContentFragment(val item: ItemForAdapter) : Fragment() {

    lateinit var binding:FragmentContentBinding
    val person:ItemForAdapter.Person = item as ItemForAdapter.Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContentBinding.inflate(inflater,container,false)
        binding.apply {
            nameFirst.text = person.firstName
            nameLast.text = person.lastName
            emaill.text = person.email
            numberPhone.text = person.phoneNumber
        }
        binding.toolbarContent.inflateMenu(R.menu.content_menu)
        binding.toolbarContent.menu.apply {
           val edit = this.findItem(R.id.edit_item)
           val delete = this.findItem(R.id.delete_item)


        }

        return inflater.inflate(R.layout.fragment_content, container, false)
    }





    companion object {

        @JvmStatic
        fun newInstance(item:ItemForAdapter) =
            ContentFragment(item)

    }
}