package com.captech.android_activity_results

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.captech.android_activity_results.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private var imageUri: Uri? = null

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
     *
     * Launches permission request for location
     */
    fun onRequestLocationClick(view: View) {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            // update image
            mBinding.iconLocationPermission.isEnabled = isGranted

            val message = if (isGranted) {
                "Location permission has been granted!"
            } else {
                "Location permission denied! :("
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    /**
     * Function for onClick from XML
     *
     * Launches permission request for camera
     */
    fun onRequestCameraClick(view: View? = null, callback: Runnable? = null) {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            // update image
            mBinding.iconCameraPermission.isEnabled = isGranted

            val message = if (isGranted) {
                "Camera permission has been granted!"
            } else {
                "Camera permission denied! :("
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if (isGranted) {
                callback?.run()
            }
        }.launch(Manifest.permission.CAMERA)
    }

    /**
     * Function for onClick from XML
     *
     * Launches permission request for microphone
     */
    fun onRequestMicClick(view: View) {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            // update image
            mBinding.iconMicrophonePermission.isEnabled = isGranted

            val message = if (isGranted) {
                "Microphone permission has been granted!"
            } else {
                "Microphone permission denied! :("
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }.launch(Manifest.permission.RECORD_AUDIO)
    }

    /**
     * Function for onClick from XML
     *
     * Launches multiple permissions request for camera, microphone, and location
     */
    fun onRequestAllClick(view: View) {
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
        }.launch(
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
        registerForActivityResult(MessageContract()) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }.launch()
    }

    /**
     * Function for onClick from XML
     *
     * Check if camera permission is granted, and if not, request it
     * Once permission granted, launches camera to take photo
     */
    fun onTakePhotoClick(view: View) {
        if (!checkPermission(Manifest.permission.CAMERA)) {
            // request camera permission first
            onRequestCameraClick(callback = takePicture)
        } else {
            takePicture.run()
        }
    }

    private val takePicture: Runnable = Runnable {
        ImageUtils.createImageFile(applicationContext)?.also {
            imageUri = FileProvider.getUriForFile(
                applicationContext,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                it
            )
            takePictureRegistration.launch(imageUri)
        }
    }

    private val takePictureRegistration =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                mBinding.photoPreview.setImageURI(imageUri)
            }
        }

    /**
     * Utility for checking if a specific permission is already granted or not
     */
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}