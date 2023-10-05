package com.iti.android_4.data.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.iti.android_4.R
import com.iti.android_4.ui.MainActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {

        val calendar = Calendar.getInstance()
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)


        val i = Intent(ctx, MainActivity::class.java)
        val message = intent!!.getStringExtra("body")

        val pendingIntent = TaskStackBuilder.create(ctx).run {
            addNextIntentWithParentStack(i)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        }
        if (ctx != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //NotificationManagerCompat.from(ctx).notify(NOTIFICATION_ID,notificationBuilder(ctx,pendingIntent))
                val manager =
                    ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(
                    NOTIFICATION_ID,
                    notificationBuilder(
                        ctx = ctx,
                        pendingIntent = pendingIntent,
                        message = message
                    )
                )
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun notificationBuilder(
        ctx: Context,
        pendingIntent: PendingIntent,
        message: String?
    ): Notification {
        return NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_1)
            .setContentTitle("Today Quote")
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(ContextCompat.getColor(ctx, R.color.teal_700))
            //.setLights(Color.RED,3000,3000)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .build()

    }

    companion object {
        const val CHANNEL_ID = "channel_ID"
        const val NOTIFICATION_ID = 1
    }
}