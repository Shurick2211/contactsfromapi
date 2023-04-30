package com.nimko.contacts_from_api

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.nimko.contacts_from_api.model.ItemForAdapter

class AdapterClickListener(
    val context: Context
):MyItemRecyclerViewAdapter.Clickable
{
    override fun onClick(item: ItemForAdapter.Person) {
        val intent = Intent(context, ContentActivity::class.java)
        intent.putExtra("person", item)
        context.startActivity(intent)
    }

    override fun onClickCall(item: ItemForAdapter.Person) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:${item.phoneNumber}")
        context.startActivity(callIntent)
    }

    @SuppressLint("IntentReset")
    override fun onClickEmail(item: ItemForAdapter.Person) {
        val intentEmail = Intent(Intent.ACTION_SENDTO).apply {
            type = "text/plain"
            data = Uri.parse("mailto:${item.email}")
            putExtra(Intent.EXTRA_SUBJECT, "Send email!")
            putExtra(Intent.EXTRA_TEXT, "Hi, ${item.firstName} ${item.lastName}! \n")
        }
        context.startActivity(intentEmail)
    }
}
