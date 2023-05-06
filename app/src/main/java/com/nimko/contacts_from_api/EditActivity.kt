package com.nimko.contacts_from_api


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.nimko.contacts_from_api.api_services.ApiClient
import com.nimko.contacts_from_api.api_services.Requestable
import com.nimko.contacts_from_api.databinding.ActivityEditBinding
import com.nimko.contacts_from_api.model.ItemForAdapter
import java.lang.Thread.sleep

class EditActivity : AppCompatActivity(), Requestable {
    private lateinit var binding: ActivityEditBinding
    private val apiClient: ApiClient = ApiClient()
    private var person:ItemForAdapter.Person? = null
    private var errMess:String? = null
    private var isEdit = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.add_contact)
        isEdit = false
        addPerson()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()
        return true
    }

    fun onClickOk(view: View){
        if(binding.firstName.text.isNotBlank()
            && binding.lastName.text.isNotBlank()
            && binding.email.text.isNotBlank()
            && binding.phoneNumber.text.isNotBlank()) {
            person = getPersonFromForm()
            if(!isEdit) {
                apiClient.createContact(person!!, this)
            } else {
                apiClient.editContact(person!!,this)
            }
            sleep(1000)
            if (errMess.isNullOrBlank()) {
                val intent = Intent()
                intent.putExtra("new_person", person)
                setResult(RESULT_OK, intent)
                finish()
            }
        } else {
            errMess = getString(R.string.error_add)
        }
        binding.errorText.text = errMess
        errMess = null
    }

    private fun getPersonFromForm(): ItemForAdapter.Person{
        val id = if(isEdit){
            person!!.id
        }else{
            null
        }
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

    override fun getRequest(request: String) {
        try {
            person = Gson().fromJson(request,ItemForAdapter.Person::class.java)
            Log.d("Create on API", person.toString())
        } catch (e: Exception){
            errMess = request
        }
    }
    private fun addPerson(){
        val intent = getIntent()
        person = intent.getSerializableExtra("personForEdit") as ItemForAdapter.Person?
        person?.let  {
            supportActionBar?.setTitle(R.string.edit_activity)
            binding.firstName.setText(it.firstName )
            binding.lastName.setText(it.lastName)
            binding.email.setText(it.email )
            binding.phoneNumber.setText(it.phoneNumber )
            isEdit = true
        }
    }
}