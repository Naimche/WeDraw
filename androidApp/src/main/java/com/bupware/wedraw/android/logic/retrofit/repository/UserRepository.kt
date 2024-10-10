package com.bupware.wedraw.android.logic.retrofit.repository

import android.util.Log
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.models.UserDevice
import com.bupware.wedraw.android.logic.retrofit.api.RetrofitClient
import com.bupware.wedraw.android.logic.retrofit.services.UserService
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {
    private val userService = RetrofitClient.getRetrofit().create(UserService::class.java)

    suspend fun getAllUsers(): List<User>? = suspendCancellableCoroutine { continuation ->
        userService.getAllUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                continuation.cancel()
            }
        })
    }

    suspend fun getUserByEmail(email:String): List<User>? = suspendCancellableCoroutine { continuation ->
        userService.getUserByEmail(email).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful){
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.i("error",t.toString())
                continuation.cancel()
            }
        })
    }

    suspend fun getUsersByGroupId(groupId:Long): List<User>? = suspendCancellableCoroutine { continuation ->
        userService.getUsersByGroupId(groupId).enqueue(object : Callback<List<User>?> {
            override fun onResponse(call: Call<List<User>?>, response: Response<List<User>?>) {
                if (response.isSuccessful){
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<User>?>, t: Throwable) {
                Log.i("error",t.toString())
                continuation.cancel()
            }
        })
    }

    suspend fun getUserById(id:String): List<User>? = suspendCancellableCoroutine { continuation ->
        userService.getUserById(id).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful){
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.i("error",t.toString())
                continuation.cancel()
            }
        })
    }

    suspend fun getUserByUsername(username:String): List<User>? = suspendCancellableCoroutine { continuation ->
        userService.getUserByUsername(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful){
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.i("error",t.toString())
                continuation.cancel()
            }
        })
    }

    suspend fun createUser(user: User): Boolean = suspendCancellableCoroutine { continuation ->
        userService.createUser(
            user
        ).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    continuation.resume(true,null)
                } else {
                    continuation.resume(false,null)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                if (t is java.io.EOFException) {
                    continuation.resume(true,null)
                } else {
                    Log.i("error",t.toString())
                    continuation.resume(false,null)
                }
            }
        })
    }

    suspend fun updateUser(user:User,email: String):Boolean = suspendCancellableCoroutine { continuation ->

        userService.updateUser(user = user, email = email).enqueue(object:Callback<Boolean>{
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

    //region UserDevice

    suspend fun getUserDeviceByUserID(userID:String): List<UserDevice>? = suspendCancellableCoroutine { continuation ->
        userService.getUserDeviceByUserID(userID).enqueue(object : Callback<List<UserDevice>> {
            override fun onResponse(call: Call<List<UserDevice>>, response: Response<List<UserDevice>>) {
                if (response.isSuccessful){
                    continuation.resume(response.body(),null)
                }
            }

            override fun onFailure(call: Call<List<UserDevice>>, t: Throwable) {
                continuation.cancel()
            }
        })
    }

    suspend fun createUserDevice(userDevice: UserDevice): Boolean = suspendCancellableCoroutine { continuation ->
        userService.createUserDevice(
            userDevice
        ).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    continuation.resume(true,null)
                } else {
                    continuation.resume(false,null)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                if (t is java.io.EOFException) {
                    continuation.resume(true,null)
                } else {
                    continuation.resume(false,null)
                }
            }
        })
    }

    //endregion
}