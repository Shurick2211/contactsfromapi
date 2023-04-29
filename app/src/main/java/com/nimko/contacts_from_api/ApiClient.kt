package com.nimko.contacts_from_api

import android.util.Log
import com.google.gson.GsonBuilder
import com.nimko.contacts_from_api.model.Person
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ApiClient {
    private val url = "http://contacts-book.eba-skm39mww.eu-central-1.elasticbeanstalk.com/contacts"
    private val client: OkHttpClient = OkHttpClient()

    fun createContact(person:Person, apiRequest: Requestable):String{
        return post(person, apiRequest)
    }

    fun editContact(person:Person, apiRequest: Requestable):String{
        return put(person, apiRequest)
    }

    fun getAllContacts(apiRequest: Requestable):String{
        return get(url, apiRequest)
    }

    fun getContactByEmail(email:String, apiRequest: Requestable):String{
        return get("$url/$email", apiRequest)
    }

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

    fun getContactByPhone(phone:String, apiRequest: Requestable):String{
        return get("$url/phones?phone=$phone", apiRequest)
    }

    private fun get(url:String, apiRequest: Requestable):String{
        val request =  Request.Builder()
            .url(url)
            .build();
        return execHttpAsync(request, apiRequest)
    }

    private fun put(person:Person, apiRequest: Requestable):String{
        val jsonRequest = GsonBuilder().create().toJson(person,Person::class.java)
        Log.d("Request Json", jsonRequest)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body: RequestBody = jsonRequest.toRequestBody(JSON)
        val request = Request.Builder().url(url).put(body).build()
        return execHttpAsync(request, apiRequest)
    }

    private fun post(person:Person, apiRequest: Requestable):String{
        val jsonRequest = GsonBuilder().create().toJson(person,Person::class.java)
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

}