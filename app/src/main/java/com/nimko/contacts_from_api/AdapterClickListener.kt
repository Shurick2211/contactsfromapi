package com.nimko.contacts_from_api

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.nimko.contacts_from_api.model.ItemForAdapter

class AdapterClickListener(
    val context: Context
)
{
    fun onClick(item: ItemForAdapter.Person) {
        val intent = Intent(context, ContentActivity::class.java)
        intent.putExtra("person", item)
        context.startActivity(intent)
    }

    fun onClickCall(item: ItemForAdapter.Person) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:${item.phoneNumber}")
        try{
            context.startActivity(callIntent)
        } catch (e:Exception){
            Log.w("Call error", e.message.toString())
            Toast.makeText(context,R.string.call_deniede, Toast.LENGTH_SHORT).show()
        }

    }


    @SuppressLint("IntentReset")
    fun onClickEmail(item: ItemForAdapter.Person) {
        val intentEmail = Intent(Intent.ACTION_SENDTO).apply {
            type = "text/plain"
            data = Uri.parse("mailto:${item.email}")
            putExtra(Intent.EXTRA_SUBJECT, "Send email!")
            putExtra(Intent.EXTRA_TEXT, "Hi, ${item.firstName} ${item.lastName}! \n")
        }
        context.startActivity(intentEmail)
    }


}
