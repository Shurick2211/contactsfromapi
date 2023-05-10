package com.nimko.contacts_from_api.adapter

import com.nimko.contacts_from_api.model.ItemForAdapter

interface ClickItem {
    fun click(item:ItemForAdapter)
}