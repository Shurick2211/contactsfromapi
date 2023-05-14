package com.nimko.contacts_from_api.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nimko.contacts_from_api.MainActivity
import com.nimko.contacts_from_api.R
import com.nimko.contacts_from_api.databinding.FragmentEditBinding
import com.nimko.contacts_from_api.model.ItemForAdapter
import com.nimko.contacts_from_api.model.MyViewModel

class EditFragment : Fragment() {
    var id: Long? = null
    lateinit var binding: FragmentEditBinding
    lateinit var person: ItemForAdapter.Person
    private var isEdit = false
    lateinit var bottomOk: View
    lateinit var model:MyViewModel
    lateinit var command:Commandable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getLong(ID)
        }
        command = activity as MainActivity
        model = (activity as MainActivity).model
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater, container, false)
        binding.toolbarEdit.setTitle(R.string.add_contact)
        binding.buttonOk.setOnClickListener {
            clickOk(it)
        }
        model.responseData.observe(viewLifecycleOwner, {
            if (it.isBlank()) {
                command.goBack()
            } else {
                binding.errorText.text = it
                bottomOk.isClickable = true
            }
        })
        return binding.root
    }

    fun clickOk(view: View) {
        if (binding.firstName.text.isNotBlank()
            && binding.lastName.text.isNotBlank()
            && binding.email.text.isNotBlank()
            && binding.phoneNumber.text.isNotBlank()
        ) {
            view.isClickable = false
            person = getPersonFromForm()
            bottomOk = view
            if (!isEdit) {
                model.createContact(person)
            } else {
                model.editContact(person)
            }
        } else {
            binding.errorText.text = getString(R.string.error_add)
        }
    }

    override fun onResume() {
        id?.let {
            person = model.getContactById(it)!!
            binding.apply {
                toolbarEdit.setTitle(R.string.edit_activity)
                firstName.setText(person.firstName)
                lastName.setText(person.lastName)
                email.setText(person.email)
                phoneNumber.setText(person.phoneNumber)
            }
            isEdit = true
        }
        super.onResume()
    }

    private fun getPersonFromForm(): ItemForAdapter.Person {
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
        private const val ID = "ID_EDIT"

        @JvmStatic
        fun newInstance(id: Long?) =
            EditFragment().apply {
                if (id != null) {
                    arguments = Bundle().apply {
                        putLong(ID, id)
                    }
                }
            }

    }
}