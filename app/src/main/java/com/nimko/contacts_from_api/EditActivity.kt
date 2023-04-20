package com.nimko.contacts_from_api


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.nimko.contacts_from_api.databinding.ActivityEditBinding
import com.nimko.contacts_from_api.model.Person
import java.lang.Thread.sleep

class EditActivity : AppCompatActivity(),Requestable {
    private lateinit var binding: ActivityEditBinding
    private val apiClient:ApiClient = ApiClient()
    private var person:Person? = null
    private var errMess:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClickOk(view: View){
        if(binding.firstName.text.isNotBlank()
            && binding.lastName.text.isNotBlank()
            && binding.email.text.isNotBlank()
            && binding.phoneNumber.text.isNotBlank()) {
            person = Person(
                binding.firstName.text.toString(),
                binding.lastName.text.toString(),
                binding.phoneNumber.text.toString(),
                binding.email.text.toString(),
                getString(R.string.app_name),
                null
            )
            apiClient.createContact(person!!, this)
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

    override fun getRequest(request: String) {
    try {
        person = Gson().fromJson(request,Person::class.java)
        Log.d("Create on API", person.toString())
    } catch (e: Exception){
        errMess = request
    }

    }
}