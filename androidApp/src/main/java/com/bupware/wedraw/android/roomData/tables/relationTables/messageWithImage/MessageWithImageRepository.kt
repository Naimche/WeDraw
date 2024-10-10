package com.bupware.wedraw.android.roomData.tables.relationTables.messageWithImage

class MessageWithImageRepository(private val MessWithImgDao: MessageWithImageDao) {

    fun getMessageWithImage(messagesId: Long): MessageWithImage {
        return MessWithImgDao.getMessageWithImage(messagesId)
    }

    suspend fun insertMessageWithImage(messageWithImage: MessageWithImage) {
        MessWithImgDao.insertMessageWithImage(messageWithImage)
    }




}