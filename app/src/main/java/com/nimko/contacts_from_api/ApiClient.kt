package com.nimko.contacts_from_api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nimko.contacts_from_api.model.Person
import kotlinx.coroutines.joinAll
import okhttp3.Call
import okhttp3.Response
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.reflect.Type

class ApiClient {
    val url = "http://contacts-book.eba-skm39mww.eu-central-1.elasticbeanstalk.com/contacts"
    var persons:List<Person> = ArrayList()
    private val client: OkHttpClient = OkHttpClient()

    fun createContact(person:Person):String{
       // val personJson = Gson().fromJson(post(person),Person::class.java)
        Log.d("Create JSON", post(person))
        return ""
    }

    fun getAllContacts():String{
        return get(url)
    }

    fun getContactByEmail(email:String):String{

        return get("$url/$email")
    }

    fun getContactByPhone(phone:String):String{

        return get("$url/phones?phone=$phone")
    }

    private fun get(url:String):String{
        val request =  Request.Builder()
            .url(url)
            .build();
        return execHttpAsync(request)
    }

    private fun post(person:Person):String{
        val jsonRequest = GsonBuilder().create().toJson(person,Person::class.java)
        Log.d("Request Json", jsonRequest)
        val JSON = "application/json; charset=utf-8".toMediaType()
        val body: RequestBody = jsonRequest.toRequestBody(JSON)
        val request = Request.Builder().url(url).post(body).build()
        return execHttpAsync(request)
    }

    private fun execHttpAsync(request: Request):String{
        var result = "undefined"

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Error http", e.stackTraceToString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {

                    if (!response.isSuccessful) {
                        Log.e("Error http",
                                " ${response.code} ${response.message}")
                        result = response.message
                    } else {
                        result = response.body!!.string()
                        Log.d("Http OK", result)
                        val sType = object : TypeToken<List<Person>>() { }.type
                        persons = Gson().fromJson(result, sType)
                        Log.d("LIST", persons.toString())
                    }
                }
            }
        })

        return result
    }

    private fun execHttpSync(request: Request):String {
        var result = "undefined"
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("Error http",
                        " ${response.code} ${response.message}")
                    result = response.message
                } else {
                    result = response.body!!.string()
                    Log.d("Http OK", result)
                }
            }
        } catch (e: IOException) {
            Log.e("Error http", e.stackTraceToString())
        }
        return result
    }
}