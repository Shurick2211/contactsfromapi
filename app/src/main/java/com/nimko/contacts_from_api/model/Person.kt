package com.nimko.contacts_from_api.model


data class Person(
    val firstName : String,
    val lastName : String,
    val phoneNumber : String,
    val email : String,
    val app : String,
    val date : String?
):java.io.Serializable