package com.nimko.contacts_from_api.model

import java.time.LocalDateTime


data class Person(
    val firstName : String,
    val lastName : String,
    val phoneNumber : String,
    val email : String,
    val app : String,
    val date : LocalDateTime?
):java.io.Serializable