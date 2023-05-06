package com.nimko.contacts_from_api.model

sealed class ItemForAdapter : java.io.Serializable{

    data class Person(
        val id:Long?,
        val firstName : String,
        val lastName : String,
        val phoneNumber : String,
        val email : String,
        val app : String,
        val date : String?
    ) : ItemForAdapter()

    data class Header(
        val title:String
    ) : ItemForAdapter()
}
