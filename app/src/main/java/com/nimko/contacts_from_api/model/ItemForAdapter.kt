package com.nimko.contacts_from_api.model

sealed class ItemForAdapter{

    data class Person(
        val id:Long?,
        val firstName : String,
        val lastName : String,
        val phoneNumber : String,
        val email : String,
        val app : String,
        val date : String?
    ):java.io.Serializable

    data class Header(
        val title:String
    )
}
