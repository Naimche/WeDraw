package com.bupware.wedraw.android.logic.retrofit.services

import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.models.UserDevice
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    @GET("/weDraw/users")
    fun getAllUsers(): Call<List<User>>

    @GET("/weDraw/users/email/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<List<User>>

    @GET("/weDraw/users/id/{id}")
    fun getUserById(@Path("id") id: String): Call<List<User>>

    @GET("/weDraw/users/username/{username}")
    fun getUserByUsername(@Path("username") username: String): Call<List<User>>

    @GET("/weDraw/users/userID/group/{groupId}")
    fun getUsersByGroupId(@Path("groupId") groupId: Long): Call<List<User>?>

    @POST("/weDraw/users")
    fun createUser(@Body user: User): Call<Boolean>


    @PUT("/weDraw/users/{email}")
    fun updateUser(@Path("email") email: String,@Body user: User): Call<Boolean>

    //region UserDevice
    @GET("/weDraw/users/userDevice/{userID}")
    fun getUserDeviceByUserID(@Path("userID") userID: String): Call<List<UserDevice>>

    @POST("/weDraw/users/userDevice")
    fun createUserDevice(@Body userDevice: UserDevice): Call<Boolean>
    //endregion

}