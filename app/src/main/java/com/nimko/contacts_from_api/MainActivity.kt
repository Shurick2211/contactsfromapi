package com.nimko.contacts_from_api

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nimko.contacts_from_api.adapter.MyItemRecyclerViewAdapter
import com.nimko.contacts_from_api.api_services.ApiClient
import com.nimko.contacts_from_api.api_services.Requestable
import com.nimko.contacts_from_api.databinding.ActivityMainBinding
import com.nimko.contacts_from_api.model.ItemForAdapter
import com.nimko.contacts_from_api.model.MyViewModel
import java.lang.Thread.sleep


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter: MyItemRecyclerViewAdapter = MyItemRecyclerViewAdapter( )
    private var startForResult:ActivityResultLauncher<Intent>? = null
    private lateinit var model:MyViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val person = intent?.getSerializableExtra("new_person") as ItemForAdapter.Person
                Log.d("My log", person.toString())
                model.apiReq()
            }
        }

        model = ViewModelProvider(this).get(MyViewModel::class.java)

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = adapter
        model.values.observe(this,{
            refreshList(it)
            Log.d("MainActivity","Observer")
        })
    }

    override fun onResume() {
        super.onResume()

    }

    private fun refreshList(allContacts: MutableList<ItemForAdapter.Person>) {
        allContacts.sortBy { it.firstName }
        var ch = allContacts[0].firstName[0]
        adapter.values.clear()
        adapter.values.add(ItemForAdapter.Header(ch.toString()))
        allContacts.forEach {
            val startCh = it.firstName[0]
            if(startCh != ch) {
                adapter.values.add(ItemForAdapter.Header(startCh.toString()))
                ch = startCh
            }
            adapter.values.add(it)
        }
        adapter.refresh()
        Log.d("List","Refresh")
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
                 if (!ch.isNullOrBlank()) {
                    adapter.values = (model.list.filter {"${it.firstName.lowercase()} ${it.lastName.lowercase()}"
                        .contains(ch.lowercase())}.toMutableList() ?: adapter.refresh( )) as MutableList<ItemForAdapter>
                } else {
                     model.values.value?.let { refreshList(it) }
                }
                return true
            }
        })
        return true
    }



    fun onClickAdd(view:View){
        startForResult?.launch(Intent(this, EditActivity::class.java))
    }


    private fun checkPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                ACCESS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Call access", "allow")
            } else {
                Toast.makeText(this,R.string.call_deniede,Toast.LENGTH_SHORT).show()
            }
        }
    }


    private companion object{
        const val ACCESS = 1
    }
}

