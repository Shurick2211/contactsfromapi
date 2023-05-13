package com.nimko.contacts_from_api.fragment

import com.nimko.contacts_from_api.model.ItemForAdapter

interface Commandable {
    fun goBack()
    fun edit(item:ItemForAdapter.Person)
}