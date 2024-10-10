package com.bupware.wedraw.android.logic.dataHandler

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.bupware.wedraw.android.core.utils.Converter
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.Image
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.models.UserDevice
import com.bupware.wedraw.android.logic.retrofit.repository.GroupRepository
import com.bupware.wedraw.android.logic.retrofit.repository.UserRepository
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.roomData.tables.message.MessageFailed
import com.bupware.wedraw.android.roomData.tables.message.MessageFailedRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageWithImageFailed
import com.bupware.wedraw.android.roomData.tables.message.MessageWithImageFailedRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.Serializable
import kotlin.system.measureTimeMillis
import com.bupware.wedraw.android.roomData.tables.group.GroupRepository as GroupRepositoryRoom
import com.bupware.wedraw.android.roomData.tables.user.UserRepository as UserRepositoryRoom
import com.bupware.wedraw.android.roomData.tables.group.Group as GroupRoom

import com.bupware.wedraw.android.roomData.tables.message.Message as MessageRoom
import com.bupware.wedraw.android.roomData.tables.user.User as UserRoom
import com.bupware.wedraw.android.logic.retrofit.repository.MessageRepository as MessageRepositoryRetrofit

class DataUtils:Serializable {
    suspend fun initData(context: Context) {


        CoroutineScope(Dispatchers.Default).launch {
            withContext(Dispatchers.Default) {
                updateDeviceID()
            }
        }



        //primero localmente a memoria
        withContext(Dispatchers.Default) {


            MemoryData.groupList = Converter.converterGroupsEntityToGroupsList(
                    getGroupsLocal(context) ?: emptyList()
                )

            MemoryData.forceGroupsUpdate.value = true


            MemoryData.userList =
                    Converter.convertUsersEntityToUsersList(
                        getUsersLocal(context) ?: emptySet()
                    )


            MemoryData.messageList =
                    getMapOfMessageByGroup(MemoryData.groupList, context).toMutableMap()


            MemoryData.uriList = getMapOfMessageUri(context).toMutableMap()

                initNotificationCounter(context)

                /**
                 * Obtengo los grupos en remoto los paso a local y los meto en memoria.
                 * Si no hay grupos en remoto no hago nada.
                 * */
                getGroupsRemote(context).also {
                    if (it != null) remoteGroupsToLocal(it, context)
                }?.let {
                    MemoryData.groupList = it.toMutableList()
                    MemoryData.forceGroupsUpdate.value = true
                }


                getUsersRemote(context).also {
                    if (it != null) remoteUsersToLocal(it, context)
                }





            }


        sendPendingMessages(context)
        sendPendingMessagesWithImage(context)


    }

    suspend fun updateDeviceID(){

        val userID = Firebase.auth.currentUser?.uid
        val token = FirebaseMessaging.getInstance().token.await()

        //Primero compruebo si este dispositivo ya está registrado
        val userDevices = withContext(Dispatchers.Default) { UserRepository.getUserDeviceByUserID(userID = userID.toString())?: emptyList()}

        if (!userDevices.any { it.deviceID == token }){
            //Si no lo está entonces lo inserto
            UserRepository.createUserDevice(UserDevice(
                id = null,
                userID = userID.toString(),
                deviceID = token
            ))

        }

    }

    suspend fun initNotificationCounter(context: Context) {

        val PREFS_NAME = "MyPrefs"
        val NOTIFICATION_COUNT_KEY = "notification_count"

        MemoryData.groupList.forEach {

            val preferences =
                context.getSharedPreferences(PREFS_NAME, FirebaseMessagingService.MODE_PRIVATE)
            MemoryData.notificationList[it.id!!] =
                preferences.getInt(NOTIFICATION_COUNT_KEY + "${it.id}", 0).toLong()


        }
    }

    fun convertImageBitmapToBitmap(bitmap: ImageBitmap): Bitmap {
        return bitmap.asAndroidBitmap()
    }

    private suspend fun sendPendingMessages(context: Context) {
        val room = WDDatabase.getDatabase(context)
        val pendingMessages = withContext(Dispatchers.Default) {
            MessageFailedRepository(room.messageFailedDao()).readAllData.first().toMutableList()
        }


        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        var index = 0
        while (index < pendingMessages.size) {
            val pendingMessage : MessageFailed = pendingMessages[index]
            val network = connectivityManager.activeNetwork

            if (network != null) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

                if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {


                    val returningId = sendPendingMessage(
                        pendingMessage.toMessage()
                    )


                    MessageFailedRepository(room.messageFailedDao()).deleteMessage(
                        pendingMessage
                    )
                    MessageRepository(room.messageDao()).insert(
                        Converter.convertMessageFailedToMessageEntity(
                            pendingMessage, returningId
                        )
                    )

                    MemoryData.messageList =
                            getMapOfMessageByGroup(MemoryData.groupList, context).toMutableMap()
                    MemoryData.forceMessagesUpdate.value = true

                        // Incrementa el índice solo si se elimina el mensaje
                        index++

                        DataHandler(context).sendPushNotification(
                            Converter.convertMessageFailedToMessageDTO(
                                optionalId = returningId,
                                message = pendingMessage
                            )
                        )
                    }
                }

            }

            // Espera antes de la siguiente iteración del bucle
            delay(5000)

        //MessageFailedRepository(room.messageFailedDao()).deleteAll()
    }

    private suspend fun sendPendingMessagesWithImage(context: Context) {
        val room = WDDatabase.getDatabase(context)
        val pendingMessages = withContext(Dispatchers.Default) {
            MessageWithImageFailedRepository(room.messageWithImageFailedDao()).readAllData.first().toMutableList()
        }


        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        var index = 0

        var imageID:Long? = null

        while (index < pendingMessages.size) {
            val pendingMessage : MessageWithImageFailed = pendingMessages[index]
            val network = connectivityManager.activeNetwork

            if (network != null) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

                if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {

                    //PRIMERO NECESITAMOS EL IMAGEID
                    //PARA ESTAS ALTURAS, SE PUEDE TENER O NO, ASÍ QUE PRIMERO COMPRUEBO SI ES NULL
                    if (pendingMessage.image_Id != null ) {imageID = pendingMessage.image_Id}
                    else if (imageID == null) {
                        imageID = withContext(Dispatchers.IO) { MessageRepositoryRetrofit.createImage(Image(id = null, byteArray = pendingMessage.bitmap!!))}
                    }

                    if (imageID != null){

                            val returningId = sendPendingMessage(
                                pendingMessage.toMessage()
                            )

                            if (returningId != null) {
                                //Lo guardo en memoria
                                DataHandler(context).saveMessageWithImageMemory(imageID = imageID,groupId = pendingMessage.owner_group_Id,uri = pendingMessage.uri.toString())
                                //Ahora lo guardo en Room local
                                DataHandler(context).saveMessageWithImageLocal(imageID, Uri.parse(pendingMessage.uri))


                                MessageWithImageFailedRepository(room.messageWithImageFailedDao()).deleteMessage(
                                    pendingMessage
                                )
                                MessageRepository(room.messageDao()).insert(
                                    Converter.convertMessageWithImageFailedToMessageEntity(
                                        pendingMessage,
                                        returningId,
                                        imageID
                                    )
                                )

                                //Recargo los mensajes
                                MemoryData.messageList =
                                    getMapOfMessageByGroup(MemoryData.groupList, context).toMutableMap()
                                MemoryData.forceMessagesUpdate.value = true

                                // Incrementa el índice solo si se elimina el mensaje
                                index++

                                DataHandler(context).sendPushNotification(
                                    Converter.convertMessageWithImageFailedToMessageDTO(
                                        optionalId = returningId,
                                        message = pendingMessage,
                                        imageID = imageID
                                    )
                                )

                            }
                    }


                }
            }

        }

        // Espera antes de la siguiente iteración del bucle
        delay(5000)

    }

    suspend fun sendSinglePendingMessageWithImage(context: Context, message:MessageWithImageFailed) {
        val room = WDDatabase.getDatabase(context)
        val pendingMessages = listOf<MessageWithImageFailed>(message)


        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        var index = 0

        var imageID:Long? = null

        while (index < pendingMessages.size) {
            val pendingMessage : MessageWithImageFailed = pendingMessages[index]
            val network = connectivityManager.activeNetwork

            if (network != null) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

                if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {

                    //PRIMERO NECESITAMOS EL IMAGEID
                    //PARA ESTAS ALTURAS, SE PUEDE TENER O NO, ASÍ QUE PRIMERO COMPRUEBO SI ES NULL
                    if (pendingMessage.image_Id != null ) {imageID = pendingMessage.image_Id}
                    else if (imageID == null) {
                        imageID = withContext(Dispatchers.IO) { MessageRepositoryRetrofit.createImage(Image(id = null, byteArray = pendingMessage.bitmap!!))}
                    }

                    if (imageID != null){

                        val returningId = sendPendingMessage(
                            pendingMessage.toMessage()
                        )

                        if (returningId != null) {
                            //Lo guardo en memoria
                            DataHandler(context).saveMessageWithImageMemory(imageID = imageID,groupId = pendingMessage.owner_group_Id,uri = pendingMessage.uri.toString())
                            //Ahora lo guardo en Room local
                            DataHandler(context).saveMessageWithImageLocal(imageID, Uri.parse(pendingMessage.uri))

                            MessageWithImageFailedRepository(room.messageWithImageFailedDao()).deleteMessage(
                                pendingMessage
                            )
                            MessageRepository(room.messageDao()).insert(
                                Converter.convertMessageWithImageFailedToMessageEntity(
                                    pendingMessage,
                                    returningId,
                                    imageID
                                )
                            )

                            //Recargo los mensajes
                            MemoryData.messageList =
                                getMapOfMessageByGroup(MemoryData.groupList, context).toMutableMap()
                            MemoryData.forceMessagesUpdate.value = true
                            // Incrementa el índice solo si se elimina el mensaje
                            index++

                            DataHandler(context).sendPushNotification(
                                Converter.convertMessageWithImageFailedToMessageDTO(
                                    optionalId = returningId,
                                    message = pendingMessage,
                                    imageID = imageID
                                )
                            )

                        }
                    }

                }
            }

            // Espera antes de la siguiente iteración del bucle
            delay(5000)
        }

    }


    suspend fun sendSinglePendingMessage(context: Context, message:MessageFailed) {
        val room = WDDatabase.getDatabase(context)
        val pendingMessages = listOf<MessageFailed>(message)

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        var index = 0
        while (index < pendingMessages.size) {
            val pendingMessage : MessageFailed = pendingMessages[index]
            val network = connectivityManager.activeNetwork
            Log.i("DataUtilsSendOffline", "1sendPendingMessages: $network")
            if (network != null) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                Log.i("DataUtilsSendOffline", "2sendPendingMessages: $networkCapabilities")
                if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {


                    val returningId = sendPendingMessage(
                        pendingMessage.toMessage()
                    )
                    Log.i("DataUtilsSendOffline", "3sendPendingMessages: $returningId")
                    if (returningId != null) {


                        Log.i("DataUtilsSendOffline", "4sendPendingMessages: $returningId")
                        MessageFailedRepository(room.messageFailedDao()).deleteMessage(
                            pendingMessage
                        )
                        MessageRepository(room.messageDao()).insert(
                            Converter.convertMessageFailedToMessageEntity(
                                pendingMessage,
                                returningId
                            )
                        )
                        // Incrementa el índice solo si se elimina el mensaje
                        index++

                        DataHandler(context).sendPushNotification(Converter.convertMessageFailedToMessageDTO(optionalId = returningId, message = pendingMessage))
                    }
                }
            }

            // Espera antes de la siguiente iteración del bucle
            delay(5000)
        }

    }

    private suspend fun sendPendingMessage(message: MessageRoom): Long? {
        return MessageRepositoryRetrofit.createMessage(
            Converter.convertMessageEntityToMessage(
                message
            )
        )
    }

    private suspend fun getMapOfMessageByGroup(
        group: MutableList<Group>,
        context: Context
    ): Map<Long, MutableList<Message>> {

        val map = mutableMapOf<Long, MutableList<Message>>()

        group.forEach { group ->
            val groupId = group.id ?: 0L // Conversión explícita de Long? a Long
            val messages = getMessagesLocalByGroupId(context, groupId)
            map[groupId] =
                messages?.let {
                    Converter.convertMessagesEntityToMessagesList(it).toMutableList()
                }!!
        }

        return map
    }

    private suspend fun getMapOfMessageUri(context: Context): Map<Long,Map<Long,Uri>>{

        val urisByGroup = mutableMapOf<Long,Map<Long,Uri>>()


        MemoryData.messageList.forEach { message ->

            val uriList = mutableMapOf<Long, Uri>()

            message.value.forEach {
                try {
                    if (it.imageID != null) {
                        uriList[it.imageID!!] = DataHandler(context).loadUrisRoom(it.imageID!!)
                    }
                } catch (e:Exception) {
                    Log.i("EXCEPTION", e.stackTraceToString())
                }
            }

            urisByGroup[message.key] = uriList

        }

        return urisByGroup
    }

    private suspend fun getMessagesLocalByGroupId(
        context: Context,
        idGroup: Long
    ): Set<MessageRoom>? {

        val database = WDDatabase.getDatabase(context)
        val messageRepository =
            com.bupware.wedraw.android.roomData.tables.message.MessageRepository(database.messageDao())

        return messageRepository.getMessagesByGroupId(idGroup).first().toSet()
    }

    private suspend fun getGroupsRemote(context: Context): List<Group>? {

        val userId = Firebase.auth.currentUser?.uid.toString()
        val groups = withContext(Dispatchers.Default) {
            GroupRepository.getGroupByUserId(userId)
        }
        return groups
    }

    private suspend fun getUsersRemote(context: Context): List<User>? {

        val userId = Firebase.auth.currentUser?.uid.toString()
        val usersTotal = mutableListOf<User>()

        MemoryData.groupList.forEach {
            val users = withContext(Dispatchers.Default){
                UserRepository.getUsersByGroupId(groupId = it.id!!)
            }
            if (users!!.isNotEmpty()) usersTotal += users
        }


        return usersTotal
    }



    private suspend fun remoteGroupsToLocal(groups: List<Group>, context: Context): Boolean {

        val database = WDDatabase.getDatabase(context)
        val groupRepository =
            GroupRepositoryRoom(database.groupDao())
        try {
            groupRepository.insertAll(Converter.converterGroupsToGroupEntityList(groups))

        } catch (e: Exception) {
            return false
        }
        return true
    }

    private suspend fun remoteUsersToLocal(users: List<User>, context: Context): Boolean {

        val database = WDDatabase.getDatabase(context)

        val userRepository =
            UserRepositoryRoom(database.userDao())
        try {
            userRepository.insertAll(Converter.converterUsersEntitiesToUser(users))

        } catch (e: Exception) {
            return false
        }
        return true
    }

    private suspend fun getGroupsLocal(context: Context): List<GroupRoom>? {

        val database = WDDatabase.getDatabase(context)
        val groupRepository =
            GroupRepositoryRoom(database.groupDao())

        return groupRepository.readAllData.first()
    }

    private suspend fun getUsersLocal(context: Context): Set<UserRoom>? {

        val database = WDDatabase.getDatabase(context)
        val userRepository =
            com.bupware.wedraw.android.roomData.tables.user.UserRepository(database.userDao())

        return userRepository.readAllData.first().toSet()
    }

    companion object {


        suspend fun gestionLogin(askForUsername: () -> Unit, context: Context) {

            val userEmail = Firebase.auth.currentUser?.email.toString()

            //Primero obtengo la información de la sesión en la BBDD
            val user = withContext(Dispatchers.Default) {
                UserRepository.getUserByEmail(userEmail)?.firstOrNull()
            }

            if (user != null) {
                DataHandler(context).saveUser(user)
            }

            if (user == null) {
                //Si no existe creamos el usuario
                withContext(Dispatchers.Default) {
                    UserRepository.createUser(
                        User(
                            id = Firebase.auth.currentUser?.uid,
                            email = userEmail,
                            premium = false,
                            username = "",
                            expireDate = null
                        )
                    )
                }
                //Acto seguido preguntamos por el username
                askForUsername()

            } else {
                //Si existe pero no tiene campo username, le pedimos que ponga un username
                if (user.username!!.isEmpty())
                    askForUsername()
                else MemoryData.user = user
            }

        }

        suspend fun sendGroupExitPush(){

        }

        fun deleteGroupInMemory(groupId:Long){
            val targetGroup = MemoryData.groupList.first { it.id == groupId }
            MemoryData.groupList.remove(targetGroup)
        }

        suspend fun updateUsername(newUsername: String): Boolean {

            var usernameExists = withContext(Dispatchers.Default) {
                UserRepository.getUserByUsername(newUsername)?.firstOrNull()
            }

            if (usernameExists != null) {
                return false
            } else {

                val userEmail = Firebase.auth.currentUser?.email.toString()
                var user = withContext(Dispatchers.Default) {
                    UserRepository.getUserByEmail(userEmail)?.firstOrNull()
                }
                user!!.username = newUsername

                if (user != null) {
                    withContext(Dispatchers.Default) {
                        UserRepository.updateUser(
                            email = userEmail,
                            user = user
                        )
                    }
                    MemoryData.user = user
                    MemoryData.user = user
                    return true
                } else {
                    Log.e("Error", "Usuario no existe, no puede actualizarse")
                    return false
                }
            }

        }
    }

    suspend fun getUserGroups(context: Context): List<Group> {
        val userId = Firebase.auth.currentUser?.uid.toString()
        Log.i("hilos", "getUserGroups")
        val group = withContext(Dispatchers.Default) {
            GroupRepository.getGroupByUserId(userId)
        } ?: emptyList()
        group.forEach { group ->
            group.userGroups?.forEach {
                Log.i(
                    "GROUPS",
                    it.userID.toString()
                )
            }
        }

        return group
    }

    private suspend fun getUsersAndSaveInLocal(context: Context, groupList: List<Group>) {
        val database = WDDatabase.getDatabase(context)
        val userRepository =
            com.bupware.wedraw.android.roomData.tables.user.UserRepository(database.userDao())
        Log.i("hilos", "getUsersAndSaveInLocal")

        groupList.forEach {
            it.userGroups?.forEach { userGroup ->
                Converter.converterUserToUserEntity(userGroup.userID)
                    ?.let { user -> userRepository.insert(user) }
            }
        }
    }



}