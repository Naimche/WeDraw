package com.bupware.wedraw.android.ui.drawingScreen

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.content.FileProvider
import androidx.datastore.dataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.roomData.tables.group.GroupRepository
import com.bupware.wedraw.android.roomData.tables.image.ImageRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageRepository
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupWithUsersRepository
import com.bupware.wedraw.android.roomData.tables.user.UserRepository
import com.bupware.wedraw.android.ui.widget.WeDrawPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.Q)
@HiltViewModel
class DrawingScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {


    var imageBitmap by savedStateHandle.saveable { mutableStateOf(ImageBitmap(1, 1)) }
    var gameList by savedStateHandle.saveable { mutableStateOf("") }

    var imageBitmapWidget by savedStateHandle.saveable { mutableStateOf(ImageBitmap(1, 1)) }


    fun insertData(context: Context, bitmap: Bitmap) = viewModelScope.launch {
        //message
//        val mwimg = WDDatabase.getDatabase(context).messageWithImageDao()
//        val repository: MessageWithImageRepository = MessageWithImageRepository(mwimg)

        //group
        val groupDao = WDDatabase.getDatabase(context).groupDao()
        val groupRepository: GroupRepository = GroupRepository(groupDao)

        //user
        val userDao = WDDatabase.getDatabase(context).userDao()
        val userRepository: UserRepository = UserRepository(userDao)

        //save image in cache and get uri
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "wdraw_$timestamp.jpg"
        val urisaved = saveImageToCache(context, bitmap, fileName)


//        insert user
//        userRepository.insert(User(userId = "testuid", name = "test"))
//
//        userRepository.readAllData.collect { ist ->
//            ist.forEach {
//
//                Log.i("database", it.userId)
//            }
//        }
//        Log.i("database", "try")

        //insert group
//        val gid = groupRepository.insert(Group(name = "testgroup", code = "testcode"))
//
//        Log.i("database", "try")
//        val group = groupRepository.readAllData
//        group.collect { ist ->
//            ist.forEach {
//                Log.i("database", it.name)
//            }
//        }

        //cross group with user
        val groupWithUsers = WDDatabase.getDatabase(context).groupWithUsersDao()
        val groupWithUserRepository: GroupWithUsersRepository =
            GroupWithUsersRepository(groupWithUsers)

//        insert group with user

//            groupWithUserRepository.insert(
//                GroupUserCrossRef(
//                    1,
//                    "testuid"
//                )
//            )


//
//
//
//
//        insert message with image
        val messageWithImage =
            WDDatabase.getDatabase(context).messageWithImageDao()

//        val messageWithImageRepository: MessageWithImageRepository =
//            MessageWithImageRepository(messageWithImage)
//        messageWithImageRepository.insertMessageWithImage(
//            MessageWithImage(
//                Image(uri =urisaved.toString()),
//                Message(owner_group_Id = 1, text = "test funciona", ownerId = "testuid", date = null)
//            )
//        )

        //message repository to readdata
        val messageDao = WDDatabase.getDatabase(context).messageDao()
        val messageRepository: MessageRepository = MessageRepository(messageDao)

//        userRepository.readAllData.onEach { ist -> Log.i("database", ist.toString()) }
//            .launchIn(viewModelScope)
//        groupRepository.readAllData.onEach { ist -> Log.i("database", ist.toString()) }
//            .launchIn(viewModelScope)
        groupWithUserRepository.getAllUserCrossRefByGroupID(1).onEach { ist -> Log.i("database", ist.toString()) }
            .launchIn(viewModelScope)
//        messageRepository.readAllData.onEach { ist -> Log.i("database", ist.toString()) }
//            .launchIn(viewModelScope)

//        dataStore.getUri.collect { ist -> Log.i("database", ist) }

    }

    fun insertDataTest(context: Context) {
        viewModelScope.launch {

        }
    }

    fun deleteData(context: Context) = viewModelScope.launch {
        val imageDao = WDDatabase.getDatabase(context).imageDao()
        val repository: ImageRepository = ImageRepository(imageDao)
//        repository.deleteAllImages()
    }


    private fun saveImageToExternalStorage(
        bitmap: Bitmap,
        filename: String,
        context: Context
    ): Uri? {
        val resolver = context.contentResolver
        val imageCollection =
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)


        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        var imageUri: Uri? = null
        resolver.insert(imageCollection, imageDetails)?.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream ->
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                    imageUri = uri
                }
            }
        }

        return imageUri
    }

    fun saveImageToCache(context: Context, bitmap: Bitmap, filename: String): Uri? {
        val cacheDirectory = context.cacheDir
        val imageFile = File(cacheDirectory, filename)

        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // Obtén el URI del archivo de imagen en la caché

        return FileProvider.getUriForFile(
            context,
            "com.bupware.wedraw.android.fileprovider",
            imageFile
        )
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}