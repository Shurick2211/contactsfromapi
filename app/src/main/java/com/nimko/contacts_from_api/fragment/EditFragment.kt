package com.nimko.contacts_from_api.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nimko.contacts_from_api.R
import com.nimko.contacts_from_api.databinding.FragmentEditBinding
import com.nimko.contacts_from_api.model.ItemForAdapter
import com.nimko.contacts_from_api.model.MyViewModel

class EditFragment(
    val id:Long?,
    val model: MyViewModel,
    val command:Commandable
) : Fragment() {

    lateinit var binding: FragmentEditBinding
    lateinit var person: ItemForAdapter.Person
    private var isEdit = false
    private var errMess:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater,container,false)
        binding.toolbarEdit.setTitle(R.string.add_contact)
        binding.buttonOk.setOnClickListener {
            clickOk(it)
        }
        return binding.root
    }

    fun clickOk(view: View){
        if(binding.firstName.text.isNotBlank()
            && binding.lastName.text.isNotBlank()
            && binding.email.text.isNotBlank()
            && binding.phoneNumber.text.isNotBlank()) {
            view.isClickable = false
            person = getPersonFromForm()
            errMess = if(!isEdit) {
                model.createContact(person)
            } else {
                model.editContact(person)
            }
            if (errMess.isNullOrBlank()) {
               command.goBack()
            }
        } else {
            errMess = getString(R.string.error_add)
        }
        binding.errorText.text = errMess
        errMess = null
        view.isClickable = true
    }

    override fun onResume() {
        if(id != null) {
            person = model.getContactById(id)!!
            binding.apply {
                toolbarEdit.setTitle(R.string.edit_activity)
                firstName.setText(person.firstName )
                lastName.setText(person.lastName)
                email.setText(person.email )
                phoneNumber.setText(person.phoneNumber )
            }
            isEdit = true
        }
        super.onResume()
    }

    private fun getPersonFromForm(): ItemForAdapter.Person{
        return ItemForAdapter.Person(
            id,
            binding.firstName.text.toString(),
            binding.lastName.text.toString(),
            binding.phoneNumber.text.toString(),
            binding.email.text.toString(),
            getString(R.string.app_name),
            null
        )
    }

    companion object {

        @JvmStatic
        fun newInstance( id:Long?, model: MyViewModel, command:Commandable) =
            EditFragment(id, model, command)

    }
}