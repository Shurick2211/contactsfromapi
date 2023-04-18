package com.nimko.contacts_from_api

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.nimko.contacts_from_api.databinding.ActivityMainBinding
import com.nimko.contacts_from_api.model.Person

class MainActivity : AppCompatActivity(), MyItemRecyclerViewAdapter.Clickable {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listInit();

    }

    private fun listInit() {
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = MyItemRecyclerViewAdapter( this);
    }

    override fun onClick(item: Person) {
        val intent = Intent(this, ContentActivity::class.java)
        intent.putExtra("person",item)
        startActivity(intent)
    }
}