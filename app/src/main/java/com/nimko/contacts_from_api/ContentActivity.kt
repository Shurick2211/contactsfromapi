package com.nimko.contacts_from_api


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.nimko.contacts_from_api.databinding.ActivityContentBinding
import com.nimko.contacts_from_api.model.Person

class ContentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addPerson()

    }

    private fun addPerson(){
        val intent = getIntent()
        val person = intent.getSerializableExtra("person") as Person?
        binding.firstName.text = person?.firstName
        binding.lastName.text = person?.lastName
        binding.email.text = person?.email
        binding.phoneNumber.text = person?.phoneNumber
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()
        return true
    }
}