package com.bupware.wedraw.android.logic.dataHandler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.models.UserGroup
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.roomData.tables.group.GroupRepository
import com.bupware.wedraw.android.roomData.tables.image.Image
import com.bupware.wedraw.android.roomData.tables.image.ImageRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageFailedRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageWithImageFailedRepository
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupUserCrossRef
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupWithUsersRepository
import com.bupware.wedraw.android.roomData.tables.user.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.io.Serializable
import java.sql.Blob
import java.sql.Date
import java.sql.SQLException
import com.bupware.wedraw.android.logic.retrofit.repository.MessageRepository as MessageRepositoryRetrofit

class DataHandler(val context: Context):Serializable {

    val room = WDDatabase.getDatabase(context = context)
    private val _groupListFlow: MutableStateFlow<Set<Group>> = MutableStateFlow(emptySet())
    val groupListFlow: Flow<Set<Group>> = _groupListFlow

    suspend fun saveUser(user: User) {

        val userRoom = com.bupware.wedraw.android.roomData.tables.user.User(
            userId = user.id.toString(),
            name = user.username.toString()
        )

        UserRepository(room.userDao()).insert(userRoom)

    }

    suspend fun saveFailedMessageWithImage(message: Message, uri: Uri, bitmap: Bitmap, idGroup: Long) {

        val roomMessageFailed =
            com.bupware.wedraw.android.roomData.tables.message.MessageWithImageFailed(
                owner_group_Id = message.groupId,
                ownerId = message.senderId,
                text = message.text,
                image_Id = message.imageID,
                date = message.date?.let { Date(it.time) },
                uri = uri.toString(),
                bitmap = bitmapToBlob(bitmap)
            )

        //Guardo el mensaje en memoria
        MemoryData.messageList[idGroup]?.add(message)

        //Guardo el mensaje en local [ROOM]
        MessageWithImageFailedRepository(room.messageWithImageFailedDao()).insert(roomMessageFailed)

        //Además, trato de enviarlo activamente

        val dataUtils = DataUtils()
        dataUtils.sendSinglePendingMessageWithImage(context = context, message = roomMessageFailed)


    }

    suspend fun saveMessage(message: Message, uri: Uri?, bitmap: Bitmap?, idGroup: Long) {

        if (message.id == null){

            val roomMessageFailed = com.bupware.wedraw.android.roomData.tables.message.MessageFailed(
                owner_group_Id = message.groupId,
                ownerId = message.senderId,
                text = message.text,
                image_Id = message.imageID,
                date = message.date?.let { Date(it.time) },
                uri = uri.toString(),
                bitmap = if (bitmap == null) null else bitmapToBlob(bitmap)
            )

            //Guardo el mensaje en memoria
            MemoryData.messageList[idGroup]?.add(message)

            //Guardo el mensaje en local [ROOM]
            MessageFailedRepository(room.messageFailedDao()).insert(roomMessageFailed)

            //Además, trato de enviarlo activamente
            val dataUtils = DataUtils()
            dataUtils.sendSinglePendingMessage(context = context,message = roomMessageFailed)

        } else {

            val roomMessage = com.bupware.wedraw.android.roomData.tables.message.Message(
                id = message.id,
                owner_group_Id = message.groupId,
                ownerId = message.senderId,
                text = message.text,
                image_Id = message.imageID,
                date = message.date?.let { Date(it.time) }
            )

            //Guardo el mensaje en memoria
            if (MemoryData.messageList[idGroup] != null){
                MemoryData.messageList[idGroup]!!.add(message)
            } else MemoryData.messageList[idGroup] = mutableListOf(message)

            //Guardo el mensaje en local [ROOM]
            MessageRepository(room.messageDao()).insert(roomMessage)

            sendPushNotification(message)

        }

    }

    suspend fun saveMessageNoPush(message: Message, uri: Uri?, bitmap: Bitmap?, idGroup: Long) {

        if (message.id == null){

            val roomMessageFailed = com.bupware.wedraw.android.roomData.tables.message.MessageFailed(
                owner_group_Id = message.groupId,
                ownerId = message.senderId,
                text = message.text,
                image_Id = message.imageID,
                date = message.date?.let { Date(it.time) },
                uri = uri.toString(),
                bitmap = if (bitmap == null) null else bitmapToBlob(bitmap)
            )

            //Guardo el mensaje en memoria
            MemoryData.messageList[idGroup]?.add(message)

            //Guardo el mensaje en local [ROOM]
            MessageFailedRepository(room.messageFailedDao()).insert(roomMessageFailed)

            //Además, trato de enviarlo activamente
            val dataUtils = DataUtils()
            dataUtils.sendSinglePendingMessage(context = context,message = roomMessageFailed)

        } else {

            val roomMessage = com.bupware.wedraw.android.roomData.tables.message.Message(
                id = message.id,
                owner_group_Id = message.groupId,
                ownerId = message.senderId,
                text = message.text,
                image_Id = message.imageID,
                date = message.date?.let { Date(it.time) }
            )

            //Guardo el mensaje en memoria
            if (MemoryData.messageList[idGroup] != null){
                MemoryData.messageList[idGroup]!!.add(message)
            } else MemoryData.messageList[idGroup] = mutableListOf(message)


            //Guardo el mensaje en local [ROOM]
            MessageRepository(room.messageDao()).insert(roomMessage)


        }

    }

    suspend fun saveBitmapLocalOS(bitmap: Bitmap):Uri{
        return withContext(Dispatchers.IO) {
            val cacheDir = context.cacheDir
            val file = File(cacheDir, "image_${System.currentTimeMillis()}.png")

            var outputStream: OutputStream? = null
            try {
                outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                outputStream?.close()
            }

            // Agregar la imagen a la galería para que esté disponible para otras aplicaciones
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath,
                file.name,
                null
            )

            // Devolver la URI de la imagen guardada
            Uri.fromFile(file)
        }
    }

    suspend fun saveMessageWithImageMemory(imageID: Long, groupId:Long, uri:String){
        try {
            val oldMap = MemoryData.uriList[groupId]!!.toMutableMap()
            oldMap[imageID] = Uri.parse(uri)
            MemoryData.uriList[groupId] = oldMap
        } catch (e:Exception){
            val newMap = mapOf(imageID to Uri.parse(uri)).toMutableMap()
            newMap[imageID] = Uri.parse(uri)
            MemoryData.uriList[groupId] = newMap
        }
    }

    suspend fun saveMessageWithImageLocal(imageID: Long,uri: Uri){
        val room = WDDatabase.getDatabase(context = context)
        DataHandler(context).saveBitmapLocal(imageID, uri)
    }

    suspend fun saveBitmapLocal(imageID:Long,uri: Uri){
        val room = WDDatabase.getDatabase(context = context)
        ImageRepository(room.imageDao()).insert(Image(
            id = imageID,
            uri = uri.toString()
        ))

    }

    suspend fun loadUrisRoom(id:Long): Uri{
        val room = WDDatabase.getDatabase(context = context)
        return Uri.parse(ImageRepository(room.imageDao()).getDrawingImage(id)!!.uri)
    }

    suspend fun sendPushNotification(message: Message){
        MessageRepositoryRetrofit.sendPushNotification(message)
    }

    suspend fun saveGroups(groups: List<Group>) {


        //Guardo en memoria
        MemoryData.groupList = groups.toMutableList()
        //AÑADO LOS NUEVOS GRUPOS Y USERGROUPS A LOCAL
        groups.forEach { group ->

            val roomGroup = com.bupware.wedraw.android.roomData.tables.group.Group(
                groupId = group.id!!, name = group.name, code = group.code
            )

            group.userGroups?.forEach { userGroup ->
                val roomUserGroup = GroupUserCrossRef(
                    groupId = group.id!!,
                    userId = userGroup.userID.id.toString(),
                    isAdmin = userGroup.isAdmin
                )
                GroupWithUsersRepository(room.groupWithUsersDao()).insert(roomUserGroup)
            }


            GroupRepository(room.groupDao()).insert(roomGroup)

        }

    }

    suspend fun saveGroupLocal(group:Group){

        //Guardo en memoria
        MemoryData.groupList.add(group)

        //Guardo group y usergroup room
        val roomGroup = com.bupware.wedraw.android.roomData.tables.group.Group(
            groupId = group.id!!, name = group.name, code = group.code
        )

        val userGroups = group.userGroups!!.filter { it.userID.id != Firebase.auth.currentUser?.uid.toString() }

        userGroups.forEach { userGroup ->
            val roomUserGroup = GroupUserCrossRef(
                groupId = group.id!!,
                userId = userGroup.userID.id.toString(),
                isAdmin = userGroup.isAdmin
            )
            GroupWithUsersRepository(room.groupWithUsersDao()).insert(roomUserGroup)
        }

        GroupRepository(room.groupDao()).insert(roomGroup)

    }

    suspend fun exitGroup(groupId: Long){
        val room = WDDatabase.getDatabase(context = context)
        GroupRepository(room.groupDao()).deleteGroup(groupId = groupId)
    }

    suspend fun deleteUserFromGroup(){
        val room = WDDatabase.getDatabase(context = context)
        GroupWithUsersRepository(room.groupWithUsersDao())
        //TODO PUSH
    }
        fun bitmapToBlob(bitmap: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()
        }

        fun blobToBitmap(blob: Blob?): Bitmap? {
            if (blob == null) return null

            return try {
                val inputStream = blob.binaryStream
                val bytes = ByteArrayOutputStream()
                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    bytes.write(buffer, 0, bytesRead)
                }
                val byteArray = bytes.toByteArray()
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            } catch (e: IOException) {
                // Manejar la excepción si ocurre un error al leer los bytes
                null
            }
        }

        fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }



}

object MemoryData{
    var forceMessagesUpdate = mutableStateOf(false)

    var forceGroupsUpdate = mutableStateOf(false)

    lateinit var user: User

    lateinit var userList : MutableSet<User>

    var groupList = mutableListOf<Group>()

    lateinit var userGroupList: MutableSet<UserGroup>

    //Map de idGrupo y los mensajes correspondientes
    var messageList = mutableMapOf<Long, MutableList<Message>>()

    var uriList = mutableMapOf<Long, Map<Long,Uri>>()

    var notificationList = mutableMapOf<Long, Long>()
}


