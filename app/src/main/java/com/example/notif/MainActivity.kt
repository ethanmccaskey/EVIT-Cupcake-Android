package com.example.notif

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myBtn = findViewById<Button>(R.id.notifBtn)
        val channelId = "My_Channel_ID"
        createNotificationChannel(channelId)

        // Create an explicit intent for an activity in this app
        val intent = Intent(this,MainActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this,0,intent,0)

        val buttonIntent = Intent(this,MyBroadcastReceiver::class.java)
        buttonIntent.apply {
            action = "Do Pending Task"
            putExtra("My Favorite Color","RED Color")
        }

        val buttonPendingIntent = PendingIntent.getBroadcast(this,0,buttonIntent,0)

        myBtn.setOnClickListener{
            val notificationBuilder = NotificationCompat.Builder(this,channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Cupcake LLC.")
                    .setContentText("Finish your checkout!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .addAction(R.drawable.ic_launcher_background,"Goto",buttonPendingIntent)

            with(NotificationManagerCompat.from(this)){
                notify(1, notificationBuilder.build())
            }
        }
    }


    private fun createNotificationChannel(channelId:String) {
        // Create the NotificationChannel, but only on API 26+ (Android 8.0) because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "callback_channel"
            val channelDescription = "Callback user from savedInstance"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId,name,importance)
            channel.apply {
                description = channelDescription
            }

            // Finally register the channel with system
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}