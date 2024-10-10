package com.bupware.wedraw.android.logic.models

import java.util.Date


data class Group(
    var id:Long?,
    val name: String,
    var code: String,
    val userGroups: Set<UserGroup>?
): java.io.Serializable



