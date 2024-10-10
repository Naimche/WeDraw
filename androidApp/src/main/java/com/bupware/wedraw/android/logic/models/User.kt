package com.bupware.wedraw.android.logic.models

import java.io.Serializable
import java.util.Date

data class User(
    var id:String?,
    val email: String?,
    var username: String?,
    var premium: Boolean?,
    var expireDate: Date?
): java.io.Serializable