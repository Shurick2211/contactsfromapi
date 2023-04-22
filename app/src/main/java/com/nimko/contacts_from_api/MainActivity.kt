package com.nimko.contacts_from_api

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nimko.contacts_from_api.databinding.ActivityMainBinding
import com.nimko.contacts_from_api.model.Person
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity(), MyItemRecyclerViewAdapter.Clickable {

    private lateinit var binding: ActivityMainBinding
    private val adapter: MyItemRecyclerViewAdapter = MyItemRecyclerViewAdapter( this)
    private var startForResult:ActivityResultLauncher<Intent>? = null

    private val apiClient:ApiClient = ApiClient()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listInit();
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val person = intent?.getSerializableExtra("new_person") as Person
                Log.d("My log", person.toString())
                adapter.addNewPerson(person)
            }
        }
    }

    private fun listInit() {
        apiClient.getAllContacts(adapter)
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = adapter;
        sleep(1000)
        adapter.refresh()
    }

    override fun onClick(item: Person) {
        val intent = Intent(this, ContentActivity::class.java)
        intent.putExtra("person",item)
        startActivity(intent)
    }

    override fun onClickCall(item: Person) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:${item.phoneNumber}")
        startActivity(callIntent)
    }

    @SuppressLint("IntentReset")
    override fun onClickEmail(item: Person) {
        val intentEmail = Intent(Intent.ACTION_SENDTO).apply {
            type = "text/plain"
            data = Uri.parse("mailto:${item.email}")
            putExtra(Intent.EXTRA_SUBJECT, "Send email!")
            putExtra(Intent.EXTRA_TEXT, "Hi, ${item.firstName} ${item.lastName}! \n")
        }
        startActivity(intentEmail)
    }

    fun onClickAdd(view:View){
        startForResult?.launch(Intent(this, EditActivity::class.java))
    }
}