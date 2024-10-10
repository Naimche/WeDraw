package com.bupware.wedraw.android.logic.retrofit.services

import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.Image
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.sql.Blob

interface MessageService {

    @GET("/weDraw/messages/id/{id}")
    fun getMessageById(@Path("id") id: Long): Call<List<Message>>

    @GET("/weDraw/messages/userGroupId/{id}")
    fun getMessageByUserGroupId(@Path("id") id: Long): Call<List<Message>?>

    @GET("/weDraw/messages/messagesFrom/{messageId}/{groupId}")
    fun getMessagesFromDate(@Path("groupId") groupID: Long,@Path("messageId") messageId: Long): Call<List<Message>?>

    @POST("/weDraw/messages")
    fun createMessage(@Body message: Message): Call<Long>

    @GET("/weDraw/messages/getImage/{imageID}")
    fun getImage(@Path("imageID") imageID: Long): Call<Image>

    @POST("/weDraw/messages/createImage")
    fun createImage(@Body image: Image): Call<Long>

    @POST("/weDraw/messages/notification")
    fun sendPushNotification(@Body message: Message): Call<Boolean>

    @PUT("/weDraw/messages/{messageId}/{userID}")
    fun updateMessageStatus(@Path("messageId") messageId: Long,@Path("userID") userID: String): Call<Boolean>

}