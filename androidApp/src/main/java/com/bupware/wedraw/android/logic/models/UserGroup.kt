package com.bupware.wedraw.android.logic.models

import java.io.Serializable

open class UserGroup(
    val id: Long?,
    val userID: User,
    val groupID: Group,
    val isAdmin: Boolean = false
) : Serializable