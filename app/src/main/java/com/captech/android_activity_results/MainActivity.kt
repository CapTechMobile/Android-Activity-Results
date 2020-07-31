package com.captech.android_activity_results

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.captech.android_activity_results.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // initialize icon views
        mBinding.iconLocationPermission.isEnabled =
            (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION))
        mBinding.iconCameraPermission.isEnabled =
            (checkPermission(Manifest.permission.CAMERA))
        mBinding.iconMicrophonePermission.isEnabled =
            (checkPermission(Manifest.permission.RECORD_AUDIO))

    }

    /**
     * Function for onClick from XML
     */
    fun onRequestLocationClick(view: View) {
        mLocationPermissionCallback.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    /**
     * Function for onClick from XML
     */
    fun onRequestCameraClick(view: View) {
        mCameraPermissionCallback.launch(Manifest.permission.CAMERA)
    }

    /**
     * Function for onClick from XML
     */
    fun onRequestMicClick(view: View) {
        mMicPermissionCallback.launch(Manifest.permission.RECORD_AUDIO)
    }

    /**
     * Function for onClick from XML
     */
    fun onRequestAllClick(view: View) {
        // You can directly ask for the permission.
        permissionCallback.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        )
    }

    /**
     * Function for onClick from XML
     */
    fun onRequestMessage(view: View) {
        messageContract.launch()
    }


    private val messageContract = registerForActivityResult(MessageContract()) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }


    /**
     * Utility for checking if a specific permission is already granted or not
     */
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    private val mLocationPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            //update image
            mBinding.iconLocationPermission.isEnabled = isGranted

            val message = if (isGranted) {
                "Location permission has been granted!"
            } else {
                "Location permission denied! :("
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

    private val mCameraPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            //update image
            mBinding.iconCameraPermission.isEnabled = isGranted

            val message = if (isGranted) {
                "Camera permission has been granted!"
            } else {
                "Camera permission denied! :("
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

    private val mMicPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            //update image
            mBinding.iconMicrophonePermission.isEnabled = isGranted

            val message = if (isGranted) {
                "Microphone permission has been granted!"
            } else {
                "Microphone permission denied! :("
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

    private val permissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->

            var permissionDetails = ""
            map.entries.forEach { entry ->
                Log.d(
                    this@MainActivity.javaClass.simpleName,
                    "Permission ${entry.key} Granted: ${entry.value}"
                )

                //add to details
                if (entry.value) permissionDetails += "\n${entry.key}"

                //check icons for updating.
                when (entry.key) {
                    Manifest.permission.ACCESS_FINE_LOCATION ->
                        mBinding.iconLocationPermission.isEnabled = entry.value
                    Manifest.permission.CAMERA ->
                        mBinding.iconCameraPermission.isEnabled = entry.value
                    Manifest.permission.RECORD_AUDIO ->
                        mBinding.iconMicrophonePermission.isEnabled = entry.value
                }
            }

            if (permissionDetails.isEmpty()) {
                Toast.makeText(
                    this,
                    "The following permissions have been granted: $permissionDetails",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}