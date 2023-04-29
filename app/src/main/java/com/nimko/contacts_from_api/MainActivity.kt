package com.nimko.contacts_from_api

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nimko.contacts_from_api.databinding.ActivityMainBinding
import com.nimko.contacts_from_api.model.Person
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity(), Requestable {

    private lateinit var binding: ActivityMainBinding
    private val adapter: MyItemRecyclerViewAdapter = MyItemRecyclerViewAdapter( )
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

    override fun onResume() {
        super.onResume()
        apiClient.getAllContacts(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView
        val persons:MutableList<Person>  = adapter.values
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(ch: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(ch: String?): Boolean {
                adapter.values = if (!ch.isNullOrBlank()) {
                    persons.filter { "${it.firstName.lowercase()} ${it.lastName.lowercase()}"
                        .contains(ch.lowercase())} as MutableList<Person>
                }else{persons}
                adapter.refresh()
                return false
            }
        })
        return true
    }



    private fun listInit() {
        apiClient.getAllContacts(this)
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = adapter
        sleep(1000)
        adapter.refresh()
    }

//    override fun onClick(item: Person) {
//        val intent = Intent(this, ContentActivity::class.java)
//        intent.putExtra("person",item)
//        startActivity(intent)
//    }
//
//    override fun onClickCall(item: Person) {
//        val callIntent = Intent(Intent.ACTION_CALL)
//        callIntent.data = Uri.parse("tel:${item.phoneNumber}")
//        startActivity(callIntent)
//    }
//
//    @SuppressLint("IntentReset")
//    override fun onClickEmail(item: Person) {
//        val intentEmail = Intent(Intent.ACTION_SENDTO).apply {
//            type = "text/plain"
//            data = Uri.parse("mailto:${item.email}")
//            putExtra(Intent.EXTRA_SUBJECT, "Send email!")
//            putExtra(Intent.EXTRA_TEXT, "Hi, ${item.firstName} ${item.lastName}! \n")
//        }
//        startActivity(intentEmail)
//    }

    fun onClickAdd(view:View){
        startForResult?.launch(Intent(this, EditActivity::class.java))
    }

    override fun getRequest(request: String) {
        val sType = object : TypeToken<List<Person>>() { }.type
        val persons = Gson().fromJson<List<Person>>(request, sType)
        Log.d("LIST", persons.toString())
        adapter.values.clear()
        adapter.values.addAll(persons)
    }
}