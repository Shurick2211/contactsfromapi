package com.nimko.contacts_from_api.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nimko.contacts_from_api.api_services.ApiClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    var values: MutableLiveData<MutableList<ItemForAdapter.Person>> = MutableLiveData()
    private val client = ApiClient()


    @OptIn(DelicateCoroutinesApi::class)
    fun apiReq() {
        GlobalScope.launch {
                val request = async {
                    client.getAllContacts()
                }.await()
                Log.d("ViewModel", request)
                val sType = object : TypeToken<List<ItemForAdapter.Person>>() {}.type
                values.postValue(Gson().fromJson<List<ItemForAdapter.Person>>(request, sType).toMutableList())
        }
    }

    fun find(ch: String?): MutableList<ItemForAdapter.Person> {
        return if (!ch.isNullOrBlank()) {
            values.value!!.filter {
                "${it.firstName.lowercase()} ${it.lastName.lowercase()}"
                    .contains(ch.lowercase())
            }.toMutableList()
        } else values.value!!.toMutableList()
    }



}