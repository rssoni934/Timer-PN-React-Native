/*
 * Designed and developed by Imran
 */

package com.imranhsn.notificationtimer.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.imranhsn.notificationtimer.R
import com.imranhsn.notificationtimer.utils.NotificationBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var notificationBuilder: NotificationBuilder

  private val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { isGranted: Boolean ->
    if (isGranted) {
      // Permission is granted. Continue the action or workflow in your
      // app.
      createNotification()
    } else {
      // Explain to the user that the feature is unavailable because the
      // features requires a permission that the user has denied. At the
      // same time, respect the user's decision. Don't link to system
      // settings in an effort to convince the user to change their
      // decision.
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    findViewById<AppCompatButton>(R.id.btn_notification).setOnClickListener {
      createNotification()
      checkNotificationPermission()
    }
  }

  private fun checkNotificationPermission() {
    when {
      ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.POST_NOTIFICATIONS
      ) == PackageManager.PERMISSION_GRANTED -> {
        // You can use the API that requires the permission.
        createNotification()
      }

      shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
        Snackbar.make(
          findViewById(R.id.root),
          "Notification blocked",
          Snackbar.LENGTH_LONG
        ).setAction("Settings") {
          // Responds to click on the action
          val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          val uri: Uri = Uri.fromParts("package", packageName, null)
          intent.data = uri
          startActivity(intent)
        }.show()
      }

      else -> {
        // The registered ActivityResultCallback gets the result of this request
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          requestPermissionLauncher.launch(
            Manifest.permission.POST_NOTIFICATIONS
          )
        }
      }
    }
  }

  // create notification using NotificationBuilder method and and bind to UI
  private fun createNotification() {
    notificationBuilder
      .createBuilder(
        title = "Super excited for Saturday match day?",
        body = "Us too, bro! Ride now, win scratch cards!",
        timeInMilli = 12000
      )
      .show()
  }
}
