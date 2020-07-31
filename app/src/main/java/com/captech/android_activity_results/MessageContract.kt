package com.captech.android_activity_results

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class MessageContract : ActivityResultContract<Unit, String>() {

    companion object {
        const val MESSAGE = "MESSAGE_CONTRACT"
    }


    override fun createIntent(context: Context, input: Unit?) =
        Intent(context, MessageActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        val message = intent?.getStringExtra(MESSAGE)
        return if (message.isNullOrEmpty()) {
            "No Message Provided"
        } else {
            message
        }
    }


}