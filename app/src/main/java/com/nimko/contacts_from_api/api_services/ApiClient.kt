package com.nimko.contacts_from_api.api_services

import android.util.Log
import com.google.gson.GsonBuilder
import com.nimko.contacts_from_api.model.ItemForAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class ApiClient {
    private val url = "http://contacts-book.eba-skm39mww.eu-central-1.elasticbeanstalk.com/contacts"
    private val client: OkHttpClient = OkHttpClient()

    fun createContact(person: ItemForAdapter.Person, apiRequest: Requestable):String{
        return post(person, apiRequest)
    }

    fun editContact(person:ItemForAdapter.Person, apiRequest: Requestable):String{
        return put(person, apiRequest)
    }

     fun getAllContacts(): String {

        return get(url)
    }

//    fun getContactByEmail(email:String, apiRequest: Requestable):String{
//        return get("$url/$email", apiRequest)
//    }

    fun deleteContact(id:Long, apiRequest: Requestable):String{
        return delete(id, apiRequest)
    }

    private fun delete(id: Long, apiRequest: Requestable): String {
        val request =  Request.Builder()
            .url("$url/$id")
            .delete()
            .build()
        return execHttpAsync(request, apiRequest)
    }

//    fun getContactByPhone(phone:String, apiRequest: Requestable):String{
//        return get("$url/phones?phone=$phone", apiRequest)
//    }

     fun get(url:String): String {
        val request =  Request.Builder()
            .url(url)
            .build();
        return execHttpSync(request)//execHttpAsync(request, apiRequest)
    }

    private fun put(person:ItemForAdapter.Person, apiRequest: Requestable):String{
        val jsonRequest = GsonBuilder().create().toJson(person,ItemForAdapter.Person::class.java)
        Log.d("Request Json", jsonRequest)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body: RequestBody = jsonRequest.toRequestBody(JSON)
        val request = Request.Builder().url(url).put(body).build()
        return execHttpAsync(request, apiRequest)
    }

    private fun post(person:ItemForAdapter.Person, apiRequest: Requestable):String{
        val jsonRequest = GsonBuilder().create().toJson(person,ItemForAdapter.Person::class.java)
        Log.d("Request Json", jsonRequest)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body: RequestBody = jsonRequest.toRequestBody(JSON)
        val request = Request.Builder().url(url).post(body).build()
        return execHttpAsync(request, apiRequest)
    }

    private fun execHttpAsync(request: Request, apiRequest: Requestable):String{
        var result = ""

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Error http", e.stackTraceToString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        result = response.body?.string() ?: "$response.code"
                        Log.w("Error http",
                            " ${response.code} ${result}")
                    } else {
                        result = response.body?.string() ?: "$response.code"
                        Log.d("Http OK", result)
                    }
                    apiRequest.getRequest(result)
                }
            }
        })
        return result
    }

    fun execHttpSync(request: Request):String {
        var result = "undefined"
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("Error http",
                        " ${response.code} ${response.message}")
                    result = response.body?.string() ?: "${response.code}"
                } else {
                    result = response.body?.string() ?: "${response.code}"
                    Log.d("Http OK", result)
                }
            }
        } catch (e: IOException) {
            Log.e("Error http", e.stackTraceToString())
        }
        return result
    }
}