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
            if(response == "200"){
                values.value!!.remove(getContactById(id))
            }
        }
    }

    fun getContactById(id:Long): ItemForAdapter.Person?{
        return values.value?.find { person ->  person.id == id}
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createContact(person: ItemForAdapter.Person):String?{
        GlobalScope.launch {
            val response = async {
                client.createContact(person)
            }.await()
            try {
                val contact = Gson().fromJson(response,ItemForAdapter.Person::class.java)
            } catch (e:Exception){
                Log.e("ViewModel", "Edit $response")
            }
        }

        return null
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun editContact(person: ItemForAdapter.Person):String?{
        GlobalScope.launch {
            val response = async {
                client.editContact(person)
            }.await()
            try {
                val contact = Gson().fromJson(response,ItemForAdapter.Person::class.java)
            } catch (e:Exception){
                Log.e("ViewModel", "Edit $response")
            }
        }
        return null
    }

}