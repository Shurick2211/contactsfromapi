package com.nimko.contacts_from_api

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.nimko.contacts_from_api.adapter.ClickItem
import com.nimko.contacts_from_api.databinding.ActivityMainBinding
import com.nimko.contacts_from_api.fragment.Commandable
import com.nimko.contacts_from_api.fragment.ContentFragment
import com.nimko.contacts_from_api.fragment.EditFragment
import com.nimko.contacts_from_api.fragment.MainFragment
import com.nimko.contacts_from_api.model.MyViewModel


class MainActivity : AppCompatActivity(), ClickItem, Commandable {

    private lateinit var binding: ActivityMainBinding
    lateinit var model: MyViewModel
   // private lateinit var navigator:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        model = ViewModelProvider(this).get(MyViewModel::class.java)

        mainFragment()
        checkPermission()
       // navigator = Navigation.findNavController(this,R.id.main_nav)
    }


    private fun mainFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, MainFragment.newInstance())
            .commit()
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



    override fun click(id:Long) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,
                ContentFragment.newInstance(
                   id))
            .addToBackStack(null)
            .commit()
    }

    override fun goBack() {

        onBackPressedDispatcher.onBackPressed()
    }

    override fun edit(id: Long?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,
                EditFragment.newInstance(
                    id))
            .addToBackStack(null)
            .commit()
    }

    companion object {
       private const val ACCESS = 1

    }
}

