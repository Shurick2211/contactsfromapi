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
                val response = async {
                    client.getAllContacts()
                }.await()
                Log.d("ViewModel", response)
                val sType = object : TypeToken<List<ItemForAdapter.Person>>() {}.type
                values.postValue(Gson().fromJson<List<ItemForAdapter.Person>>(response, sType).toMutableList())
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

    @OptIn(DelicateCoroutinesApi::class)
    fun delete(id: Long){
        GlobalScope.launch {
            val response = async {
                client.deleteContact(id)
            }.await()
            Log.d("ViewModel", response)
            if(response == "200"){
                values.value!!.remove(getContactById(id))
            }
        }
    }

    fun getContactById(id:Long): ItemForAdapter.Person?{
        return values.value?.find { person ->  person.id == id}
    }

    fun createContact(person: ItemForAdapter.Person):String?{
        Log.d("ViewModel", "Create $person")
        return null
    }

    fun editContact(person: ItemForAdapter.Person):String?{
        Log.d("ViewModel", "Edit $person")
        return null
    }

}