package com.nimko.contacts_from_api.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nimko.contacts_from_api.api_services.ApiClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    var values: MutableLiveData<MutableList<ItemForAdapter.Person>> = MutableLiveData()


    private val client = ApiClient()

    init {
        apiReq()
    }

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

    fun addPerson(person:ItemForAdapter.Person){
        values.value!!.add(person)
        values.postValue(values.value)
    }

    fun removePerson(person:ItemForAdapter.Person){
        values.value!!.remove(person)
        values.postValue(values.value)
    }

}