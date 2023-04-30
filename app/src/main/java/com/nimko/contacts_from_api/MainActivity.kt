package com.nimko.contacts_from_api

import android.app.Activity
import android.content.Intent
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
import com.nimko.contacts_from_api.model.ItemForAdapter
import java.lang.Thread.sleep

lateinit var allContacts:MutableList<Any>
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
        allContacts.add(0,ItemForAdapter.Header("Hello!"))
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val person = intent?.getSerializableExtra("new_person") as ItemForAdapter.Person
                Log.d("My log", person.toString())
                allContacts.add(person)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(!adapter.values.containsAll(allContacts)
            || !allContacts.containsAll(adapter.values)) {
            adapter.addAllPersons(allContacts as Collection<ItemForAdapter>)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(ch: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(ch: String?): Boolean {
                adapter.values = if (!ch.isNullOrBlank()) {
                    allContacts
                        .filter { it is ItemForAdapter.Person }
                        .map{it as ItemForAdapter.Person}
                        .filter {"${it.firstName.lowercase()} ${it.lastName.lowercase()}"
                        .contains(ch.lowercase())} as MutableList<ItemForAdapter>
                }else{allContacts as MutableList<ItemForAdapter>}
                adapter.refresh()
                return false
            }
        })
        return true
    }

    private fun listInit() {

        makeRequestApi()
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = adapter
    }

    private fun makeRequestApi(){
        apiClient.getAllContacts(this)
        sleep(1000)
        adapter.addAllPersons(allContacts as Collection<ItemForAdapter>)
    }

    fun onClickAdd(view:View){
        startForResult?.launch(Intent(this, EditActivity::class.java))
    }

    override fun getRequest(request: String) {
        val sType = object : TypeToken<List<ItemForAdapter.Person>>() { }.type
        allContacts = Gson().fromJson<List<ItemForAdapter.Person>>(request, sType).toMutableList()

    }
}

