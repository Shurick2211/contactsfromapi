package com.nimko.contacts_from_api


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nimko.contacts_from_api.databinding.ActivityEditBinding
import com.nimko.contacts_from_api.model.Person

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    fun onClickOk(view: View){
        val person = Person(
            binding.firstName.text.toString(),
            binding.lastName.text.toString(),
            binding.phoneNumber.text.toString(),
            binding.email.text.toString(),
            "my_app",
            null
        )
        val intent = Intent()
        intent.putExtra("new_person", person)
        setResult(RESULT_OK, intent)
        finish()
    }
}