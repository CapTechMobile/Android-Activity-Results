package com.captech.android_activity_results

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

/**
 * Contract that extends ActivityResultContract to implement a custom
 * intent creation and handling of the results from that intent.
 */
class MessageContract : ActivityResultContract<Unit, String>() {

    companion object {
        const val MESSAGE = "MESSAGE_CONTRACT"
    }

    /**
     * Create an intent to start MessageActivity. If you were providing a starting point
     * you would pass that text here.
     */
    override fun createIntent(context: Context, input: Unit?) =
        Intent(context, MessageActivity::class.java)

    /**
     * Gets hit when the MessageActivity is closed. The result can then be parsed and
     * set to the String that our contract is looking for.
     */
    override fun parseResult(resultCode: Int, intent: Intent?): String {
        val message = intent?.getStringExtra(MESSAGE)
        return if (message.isNullOrEmpty()) {
            "No Message Provided"
        } else {
            message
        }
    }
}