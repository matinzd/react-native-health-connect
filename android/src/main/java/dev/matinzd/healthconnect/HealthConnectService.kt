package dev.matinzd.healthconnect

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.facebook.react.HeadlessJsTaskService
import com.facebook.react.bridge.Arguments
import com.facebook.react.jstasks.HeadlessJsTaskConfig


class HealthConnectService : HeadlessJsTaskService() {
  override fun getTaskConfig(intent: Intent?): HeadlessJsTaskConfig? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createChannel()
      val notification: Notification = NotificationCompat.Builder(applicationContext, "demo")
        .setContentTitle("Headless Work")
        .setTicker("run")
        .setOngoing(true)
        .build()
      startForeground(1, notification)
    }

    return intent?.extras?.let {
      HeadlessJsTaskConfig("HealthConnectTask", Arguments.fromBundle(it), 5000, true)
    }
  }

  // create notification to inform user about background task, required for Android 8 and up
  private fun createChannel() {
    val description = "test channel"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel("demo", "test", importance)
    channel.description = description
    val notificationManager =
      applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
  }
}
