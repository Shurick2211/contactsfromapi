package com.nimko.contacts_from_api

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nimko.contacts_from_api.adapter.ClickItem
import com.nimko.contacts_from_api.databinding.ActivityMainBinding
import com.nimko.contacts_from_api.fragment.ContentFragment
import com.nimko.contacts_from_api.fragment.MainFragment
import com.nimko.contacts_from_api.model.ItemForAdapter


class MainActivity : AppCompatActivity(), ClickItem {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        mainFragment()
    }

    override fun onBackPressed() {
        mainFragment()
    }

    private fun mainFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, MainFragment.newInstance(this))
            .commit()
        checkPermission()
    }
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CALL_PHONE),
                ACCESS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Call access", "allow")
            } else {
                Toast.makeText(this, R.string.call_deniede, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private companion object {
        const val ACCESS = 1
    }

    override fun click(item: ItemForAdapter) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, ContentFragment.newInstance(item))
            .commit()
    }
}

