package com.bupware.wedraw.android.core.api

import android.util.Log
import com.bupware.wedraw.android.core.utils.Converter
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.dataHandler.MemoryData
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.retrofit.repository.UserRepository
import com.bupware.wedraw.android.logic.retrofit.repository.MessageRepository as MessageRepositoryRetrofit
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PushNotificationManager : FirebaseMessagingService() {


    @Override
    override fun onMessageReceived(message: RemoteMessage) {

        val context = applicationContext

        CoroutineScope(Dispatchers.IO).launch {

            val imageId = if (message.data["imageId"].toString() == "null") null else message.data["imageId"]!!.toLong()

            //region Se añade el usuario si no existia
            val isUserInMemory = MemoryData.userList.firstOrNull { it.id ==  message.data["senderId"].toString()}
            if (isUserInMemory == null) {
                val user = withContext(Dispatchers.IO) { UserRepository.getUserById(message.data["senderId"].toString())!!.first()}
                MemoryData.userList.add(user)
            }
            //endregion

            DataHandler(context).saveMessageNoPush(
                idGroup = message.data["groupId"]!!.toLong()
                ,message = Message(
                    id = message.data["id"]!!.toLong(),
                    text = message.data["text"].toString(),
                    timeZone = null,
                    senderId = message.data["senderId"].toString(),
                    imageID = imageId,
                    groupId = message.data["groupId"]!!.toLong(),
                    date = Converter.parseDate(message.data["date"].toString()),
                    bitmap = null
                ), uri = null, bitmap = null
            )

            //Si es un mensaje con imagen se pide la imagen para descargar a la BBDD

            if (imageId != null) {
                val bitmap = withContext(Dispatchers.IO) {MessageRepositoryRetrofit.getImage(imageId)!!.byteArray}
                val uri = DataHandler(context).saveBitmapLocalOS(DataHandler(context).byteArrayToBitmap(bitmap!!))
                DataHandler(context).saveBitmapLocal(imageId!!,uri)
                DataHandler(context).saveMessageWithImageMemory(imageID = imageId, groupId = message.data["groupId"]!!.toLong(), uri = uri.toString())
            }

            MemoryData.forceMessagesUpdate.value = true

        }

    }


    @Override
    override fun onNewToken(token: String) {
        //Log.i("wawa", "FCM token: $token")

    }
}