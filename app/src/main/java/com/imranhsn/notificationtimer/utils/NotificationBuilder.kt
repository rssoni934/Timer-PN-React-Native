/*
 * Designed and developed by Imran
 */

package com.imranhsn.notificationtimer.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.imranhsn.notificationtimer.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationBuilder @Inject constructor(
  @ApplicationContext val context: Context
) {
  private val CHANNEL_ID = context.getString(R.string.IDS_CHANNEL_ID)
  private val CHANNEL_NAME = context.getString(R.string.IDS_CHANNEL_NAME)
  private val NOTIFICATION_ID = 8011
  private val ICON = R.drawable.ic_launcher_foreground

  @Volatile
  private lateinit var channel: NotificationChannel
  private lateinit var builder: NotificationCompat.Builder

  private val notificationManager: NotificationManagerCompat by lazy {
    NotificationManagerCompat.from(context)
  }

  // create notification channel
  private fun createChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
      ).apply {
        setSound(null, null) // disable sound
        enableLights(true) // enable light when create notification
        enableVibration(false) // disable vibration
      }
      val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      nm.createNotificationChannel(channel)
    }
  }

  // create notification builder using
  fun createBuilder(
    title: String,
    body: String,
    timeInMilli: Long
  ) = apply {
    // create custom view for notification
    val remoteViews = RemoteViews(context.packageName, R.layout.notification_content).apply {
      setChronometer(
        R.id.notification_timer,
        SystemClock.elapsedRealtime() + timeInMilli,
        null,
        true
      )
      setChronometerCountDown(R.id.notification_timer, true)
      setTextViewText(R.id.notification_title, title)
      setTextViewText(R.id.notification_body, body)
    }
    builder = NotificationCompat
      .Builder(context, CHANNEL_ID)
      .setSmallIcon(ICON)
      .setStyle(NotificationCompat.DecoratedCustomViewStyle())
      .setCustomContentView(remoteViews)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setAutoCancel(true)
      .setOngoing(false)
      .setShowWhen(true)
      .setTimeoutAfter(timeInMilli)
  }

  // show the notification
  @SuppressLint("MissingPermission")
  fun show(): Int {
    createChannel()
    notificationManager.notify(NOTIFICATION_ID, builder.build())
    return NOTIFICATION_ID
  }
}
