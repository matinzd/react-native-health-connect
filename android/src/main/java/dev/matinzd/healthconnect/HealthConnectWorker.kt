package dev.matinzd.healthconnect

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import androidx.work.Worker
import androidx.work.WorkerParameters


class HealthConnectWorker(
  val context: Context, workerParams: WorkerParameters,
) : Worker(context, workerParams) {

  override fun doWork(): Result {
    val service = Intent(context, HealthConnectService::class.java)

    val extras = Bundle()
    extras.putInt("result", 12)

    service.putExtras(extras);

    createChannel();

    context.startForegroundService(service)

    return Result.success()
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
