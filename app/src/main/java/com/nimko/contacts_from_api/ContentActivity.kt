package com.nimko.contacts_from_api


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.nimko.contacts_from_api.databinding.ActivityContentBinding
import com.nimko.contacts_from_api.model.ItemForAdapter

class ContentActivity : AppCompatActivity(), Requestable {
    private lateinit var binding : ActivityContentBinding
    var person:ItemForAdapter.Person? = null
    private var startForResult: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.content_activity)
        val intent = getIntent()
        person = intent.getSerializableExtra("person") as ItemForAdapter.Person?
        addPerson()
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                person = intent?.getSerializableExtra("new_person") as ItemForAdapter.Person
                Log.d("My log content", person.toString())
                addPerson()
            }
        }
    }

    private fun addPerson(){
        binding.apply {
            firstName.text = person?.firstName
            lastName.text = person?.lastName
            email.text = person?.email
            phoneNumber.text = person?.phoneNumber
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.edit_item -> edit()
            R.id.delete_item -> delete()
        }
        return true
    }

    private fun delete() {
        ApiClient().deleteContact(person?.id!!,this)
        allContacts.remove(person)
    }

    private fun edit() {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("personForEdit", person)
        startForResult?.launch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.content_menu, menu)
        return true
    }

    override fun getRequest(request: String) {
        finish()
    }
}