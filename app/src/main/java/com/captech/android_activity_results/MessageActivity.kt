package com.captech.android_activity_results

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.captech.android_activity_results.databinding.ActivityMessageBinding

class MessageActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    /**
     * launched onClick in the XML
     * sets this finishing activity's result to the text string entered by the user
     */
    fun onSubmitMessage(view: View) {
        val result =
            Intent().putExtra(MessageContract.MESSAGE, mBinding.messageInput.text.toString())
        setResult(Activity.RESULT_OK, result)
        finish()
    }

}