package com.nimko.contacts_from_api.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nimko.contacts_from_api.R
import com.nimko.contacts_from_api.databinding.FragmentContentBinding
import com.nimko.contacts_from_api.model.ItemForAdapter
import com.nimko.contacts_from_api.model.MyViewModel

class ContentFragment(
    val id:Long,
    val command:Commandable,
    val model:MyViewModel
    ) : Fragment() {

    lateinit var binding:FragmentContentBinding
    lateinit var person:ItemForAdapter.Person


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentBinding.inflate(inflater,container,false)

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

    override fun onResume() {
        person = model.getContactById(id)!!
        binding.apply {
            nameFirst.text = person.firstName
            nameLast.text = person.lastName
            emaill.text = person.email
            numberPhone.text = person.phoneNumber
        }
        super.onResume()
    }


    private fun delete() {
        model.delete(person.id!!)
        command.goBack()
    }

    private fun edit() {
        command.edit(person)
    }


    companion object {

        @JvmStatic
        fun newInstance(id: Long, command:Commandable, model:MyViewModel) =
            ContentFragment(id, command, model)

    }
}