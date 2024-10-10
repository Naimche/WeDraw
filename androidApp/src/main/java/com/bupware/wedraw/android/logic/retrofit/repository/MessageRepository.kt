package com.bupware.wedraw.android.logic.retrofit.repository

import android.util.Log
import com.bupware.wedraw.android.logic.models.Image
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.retrofit.api.RetrofitClient
import com.bupware.wedraw.android.logic.retrofit.services.MessageService
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Blob

object MessageRepository {
    private val messageService = RetrofitClient.getRetrofit().create(MessageService::class.java)

    suspend fun getMessageById(id:Long): List<Message>? = suspendCancellableCoroutine { continuation ->
        messageService.getMessageById(id).enqueue(object : Callback<List<Message>?> {
            override fun onResponse(call: Call<List<Message>?>, response: Response<List<Message>?>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<Message>?>, t: Throwable) {
                continuation.cancel()
            }
        })
    }

    suspend fun getMessageByUserGroupId(id:Long): List<Message>? = suspendCancellableCoroutine { continuation ->
        messageService.getMessageByUserGroupId(id).enqueue(object : Callback<List<Message>?> {
            override fun onResponse(call: Call<List<Message>?>, response: Response<List<Message>?>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<Message>?>, t: Throwable) {
                continuation.cancel()
            }
        })
    }

    suspend fun getMessagesFromDate(messageId:Long,groupId:Long): List<Message>? = suspendCancellableCoroutine { continuation ->
        messageService.getMessagesFromDate(groupId,messageId).enqueue(object : Callback<List<Message>?> {
            override fun onResponse(call: Call<List<Message>?>, response: Response<List<Message>?>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<Message>?>, t: Throwable) {
                continuation.resume(null,null)
            }
        })
    }

    suspend fun createMessage(message: Message): Long? = suspendCancellableCoroutine { continuation ->
        messageService.createMessage(message).enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                continuation.resume(null,null)
            }
        })
    }

    suspend fun getImage(imageID: Long): Image? = suspendCancellableCoroutine { continuation ->
        messageService.getImage(imageID).enqueue(object : Callback<Image> {
            override fun onResponse(call: Call<Image>, response: Response<Image>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<Image>, t: Throwable) {
                Log.i("wawa",t.stackTraceToString())
                continuation.resume(null,null)
            }
        })
    }

    suspend fun createImage(image: Image): Long? = suspendCancellableCoroutine { continuation ->
        messageService.createImage(image).enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                continuation.resume(null,null)
            }
        })
    }

    suspend fun sendPushNotification(message: Message): Boolean? = suspendCancellableCoroutine { continuation ->
        messageService.sendPushNotification(message).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                continuation.resume(null,null)
            }
        })
    }

    suspend fun updateMessageStatus(messageId:Long,userId: String):Boolean = suspendCancellableCoroutine { continuation ->

        messageService.updateMessageStatus(messageId,userId).enqueue(object:Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful){
                    continuation.resume(true,null)
                } else {
                    continuation.resume(false,null)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                if (t is java.io.EOFException) {
                    continuation.resume(true,null)
                }
                else {
                    continuation.resume(false,null)
                }
            }

        })

    }



}