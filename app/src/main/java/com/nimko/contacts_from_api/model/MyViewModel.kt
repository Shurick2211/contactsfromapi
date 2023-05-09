package com.nimko.contacts_from_api.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nimko.contacts_from_api.api_services.ApiClient
import kotlinx.coroutines.*
import java.lang.Thread.sleep

class MyViewModel:ViewModel() {
    var values:MutableLiveData<MutableList<ItemForAdapter.Person>> = MutableLiveData()
    var list:MutableList<ItemForAdapter.Person> = ArrayList()

    private val client = ApiClient()

    init {
        apiReq()
    }

    fun apiReq(){
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO){
                val request = async {
                    client.getAllContacts()
                }.await()
                Log.d("ViewModel", request)
                val sType = object : TypeToken<List<ItemForAdapter.Person>>() {}.type
                list=Gson().fromJson<List<ItemForAdapter.Person>>(request, sType).toMutableList()
            }
            values.value = list
        }
    }



}