package com.nimko.contacts_from_api


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nimko.contacts_from_api.databinding.ActivityEditBinding
import com.nimko.contacts_from_api.model.Person

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private val apiClient:ApiClient = ApiClient()
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
            val person = Person(
                binding.firstName.text.toString(),
                binding.lastName.text.toString(),
                binding.phoneNumber.text.toString(),
                binding.email.text.toString(),
                getString(R.string.app_name),
                null
            )
            apiClient.createContact(person)
            val intent = Intent()
            intent.putExtra("new_person", person)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            binding.errorText.text = getString(R.string.error_add)
        }
    }
}