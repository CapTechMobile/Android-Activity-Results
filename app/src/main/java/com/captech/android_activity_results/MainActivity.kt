package com.captech.android_activity_results

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.captech.android_activity_results.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // initialize icon views
        updateLocationIcon(checkLocationPermissions())
        updateCameraIcon(checkCameraPermissions())
        updateMicIcon(checkMicPermissions())
    }

    fun onRequestLocationClick(view: View) {
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, mLocationPermissionCallback)
    }

    fun onRequestCameraClick(view: View) {
        requestPermission(Manifest.permission.CAMERA, mCameraPermissionCallback)
    }

    fun onRequestMicClick(view: View) {
        requestPermission(Manifest.permission.RECORD_AUDIO, mMicPermissionCallback)
    }

    fun onRequestAllClick(view: View) {
        when {
            checkLocationPermissions() && checkCameraPermissions() && checkMicPermissions() -> {
                Toast.makeText(
                    this,
                    "All permissions already granted!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                    || shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                    || shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {

                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alertdialog_permission_request_heading)
                    .setMessage(R.string.alertdialog_permissions_request_description)
                    .setNegativeButton(R.string.button_cancel) { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton(R.string.button_ok) { _, _ ->
                        mAllPermissionsCallback.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO
                            )
                        )
                    }
                builder.create().show()

            }
            else -> {
                // You can directly ask for the permission.
                mAllPermissionsCallback.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                    )
                )
            }
        }
    }

    private fun requestPermission(permission: String, callback: ActivityResultLauncher<String>) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.

                Toast.makeText(
                    this,
                    "Permission $permission already granted!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            shouldShowRequestPermissionRationale(permission) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.

                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alertdialog_permission_request_heading)
                    .setMessage(R.string.alertdialog_permission_request_description)
                    .setNegativeButton(R.string.button_cancel) { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton(R.string.button_ok) { _, _ ->
                        // request permission again
                        callback.launch(permission)
                    }
                builder.create().show()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                callback.launch(permission)
            }
        }
    }

    /**
     * @return true if the app has been granted location permissions
     */
    private fun checkLocationPermissions(): Boolean {
        return checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    /**
     * @return true if the app has been granted camera permissions
     */
    private fun checkCameraPermissions(): Boolean {
        return checkPermission(Manifest.permission.CAMERA)
    }

    /**
     * @return true if the app has been granted microphone permissions
     */
    private fun checkMicPermissions(): Boolean {
        return checkPermission(Manifest.permission.RECORD_AUDIO)
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun updateLocationIcon(enabled: Boolean) {
        updateIcon(
            enabled,
            mBinding.iconLocationPermission,
            R.drawable.ic_location_on,
            R.drawable.ic_location_off
        )
    }

    private fun updateCameraIcon(enabled: Boolean) {
        updateIcon(
            enabled,
            mBinding.iconCameraPermission,
            R.drawable.ic_camera_on,
            R.drawable.ic_off
        )
    }

    private fun updateMicIcon(enabled: Boolean) {
        updateIcon(
            enabled,
            mBinding.iconMicrophonePermission,
            R.drawable.ic_mic_on,
            R.drawable.ic_mic_off
        )
    }

    /**
     * update the imageview's drawable and tint depending on the @param {enabled}
     *
     * @param enabled true if the permission this imageView represents has been granted
     */
    private fun updateIcon(
        enabled: Boolean,
        imageView: ImageView,
        enabledIcon: Int,
        disabledIcon: Int
    ) {
        imageView.setColorFilter(
            ContextCompat.getColor(this, if (enabled) R.color.enabled else R.color.disabled),
            PorterDuff.Mode.SRC_IN
        )
        imageView.setImageDrawable(
            ContextCompat.getDrawable(this, if (enabled) enabledIcon else disabledIcon)
        )
    }

    private val mLocationPermissionCallback: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            updateLocationIcon(isGranted)

            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
                Toast.makeText(
                    this,
                    "Location permission has been granted!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Toast.makeText(this, "Location permission denied! :(", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private val mCameraPermissionCallback: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            updateCameraIcon(isGranted)

            if (isGranted) {
                Toast.makeText(
                    this,
                    "Camera permission has been granted!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(this, "Camera permission denied! :(", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private val mMicPermissionCallback: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            updateMicIcon(isGranted)

            if (isGranted) {
                Toast.makeText(
                    this,
                    "Microphone permission has been granted!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(this, "Microphone permission denied! :(", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private val mAllPermissionsCallback =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            updateLocationIcon(checkLocationPermissions())
            updateCameraIcon(checkCameraPermissions())
            updateMicIcon(checkMicPermissions())
            var permissionDetails: String = ""
            for (entry in map.entries) {
                Log.d(
                    this@MainActivity.javaClass.simpleName,
                    "Permission ${entry.key} Granted: ${entry.value}"
                )
                if (entry.value) permissionDetails += "\n${entry.key}"
            }

            val msg = if (permissionDetails.isEmpty()) {
                "All permissions have already been granted / denied by the user."
            } else {
                "The following permissions have been granted: $permissionDetails"
            }

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
}