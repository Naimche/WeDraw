package com.bupware.wedraw.android.roomData.tables.image

import com.bupware.wedraw.android.roomData.tables.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class ImageRepository(private val imageDao: ImageDao) {
    suspend fun insert(image: Image) = imageDao.insertImage(image)


    val readAllData : Flow<List<Image>> = imageDao.readAllData()

    suspend fun getDrawingImage(imageid: Long): Image? {
        return withContext(Dispatchers.IO) {

            imageDao.getDrawingImage(imageid)
        }
    }


//    val readAllData: Flow<List<Image>> = imageDao.readAllData()

//    suspend fun deleteAllImages() {
//        imageDao.deleteAllImages()
//    }
}