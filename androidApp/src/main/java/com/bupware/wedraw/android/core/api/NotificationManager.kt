package com.bupware.wedraw.android.core.api

import android.content.Context
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.dataHandler.MemoryData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationManager : FirebaseMessagingService() {
    private val PREFS_NAME = "MyPrefs"
    private val NOTIFICATION_COUNT_KEY = "notification_count"

    override fun onMessageReceived(message: RemoteMessage) {

        val idGroup = message.data["groupId"]!!.toLong()

        val currentCount = getNotificationCount(idGroup) + 1
        saveNotificationCount(currentCount, groupId = idGroup)

        //Los setteo en la var de DataHandler por si se recibe la notificación y se está In-App
        MemoryData.notificationList[idGroup] = currentCount + 1.toLong()

    }

    private fun showNotification(message: RemoteMessage) {
        // Mostrar la notificación en la bandeja de entrada
        // Implementar el código para mostrar la notificación según tus necesidades
    }

    private fun getNotificationCount(groupId:Long): Int {
        val preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return preferences.getInt(NOTIFICATION_COUNT_KEY + "$groupId", 0)
    }

    private fun saveNotificationCount(count: Int, groupId:Long) {
        val preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(NOTIFICATION_COUNT_KEY + "$groupId", count)
        editor.apply()
    }

    companion object {

        private val PREFS_NAME = "MyPrefs"
        private val NOTIFICATION_COUNT_KEY = "notification_count"
        fun resetNotificationCount(groupId: Long,context: Context) {
            val preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putInt(NOTIFICATION_COUNT_KEY + "$groupId", 0)
            editor.apply()
        }
    }
}