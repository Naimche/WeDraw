package com.bupware.wedraw.android.roomData.tables.image

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image) : Long

    @Query("SELECT id, uri FROM images_table WHERE id = :imageid")
    fun getDrawingImage(imageid: Long): Image?



    @Query("SELECT * FROM images_table ORDER BY id ASC")
    fun readAllData(): Flow<List<Image>>
    @Query("DELETE FROM images_table")
    suspend fun deleteAllImages()

}