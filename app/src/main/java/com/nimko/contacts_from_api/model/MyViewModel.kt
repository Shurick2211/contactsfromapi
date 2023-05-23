package com.nimko.contacts_from_api.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nimko.contacts_from_api.api_services.ApiClient
import kotlinx.coroutines.*

class MyViewModel : ViewModel() {
    var values: MutableLiveData<MutableList<ItemForAdapter.Person>> = MutableLiveData()
    var responseData: MutableLiveData<String> = MutableLiveData()
    private val client = ApiClient()


    @OptIn(DelicateCoroutinesApi::class)
    fun apiReq() {
        GlobalScope.launch(Dispatchers.IO) {
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
        GlobalScope.launch(Dispatchers.IO) {
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
    fun createContact(person: ItemForAdapter.Person){
        GlobalScope.launch(Dispatchers.IO) {
            val response = async {
                client.createContact(person)
            }.await()
            try {
                val contact = Gson().fromJson(response,ItemForAdapter.Person::class.java)
                values.value!!.add(contact)
                responseData.postValue("")
            } catch (e:Exception){
                responseData.postValue(response)
                Log.w("ViewModel", "Create error $response")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun editContact(person: ItemForAdapter.Person){
        val index = values.value!!.indexOf(getContactById(person.id!!))
        GlobalScope.launch(Dispatchers.IO) {
            val response = async {
                client.editContact(person)
            }.await()
            try {
                val contact = Gson().fromJson(response,ItemForAdapter.Person::class.java)
                values.value!!.set( index, contact)
                responseData.postValue("")
            } catch (e:Exception){
                responseData.postValue(response)
                Log.w("ViewModel", "Edit error $response")
            }
        }
    }

}