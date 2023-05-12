package com.nimko.contacts_from_api.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.nimko.contacts_from_api.MainActivity
import com.nimko.contacts_from_api.R
import com.nimko.contacts_from_api.api_services.ApiClient
import com.nimko.contacts_from_api.databinding.FragmentContentBinding
import com.nimko.contacts_from_api.model.ItemForAdapter
import com.nimko.contacts_from_api.model.MyViewModel

class ContentFragment(
    val item: ItemForAdapter,
    val command:Commandable,
    val model:MyViewModel
    ) : Fragment() {

    lateinit var binding:FragmentContentBinding
    val person:ItemForAdapter.Person = item as ItemForAdapter.Person


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
        binding.toolbarContent.setTitle(R.string.content_activity)

        binding.toolbarContent.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.edit_item -> edit()
                R.id.delete_item -> delete()
            }
            return@setOnMenuItemClickListener false
        }

        return binding.root
    }


    private fun delete() {

        command.goBack()
    }

    private fun edit() {
        Log.d("MenuContent", "edit")
    }


    companion object {

        @JvmStatic
        fun newInstance(item:ItemForAdapter, command:Commandable, model:MyViewModel) =
            ContentFragment(item, command, model)

    }
}