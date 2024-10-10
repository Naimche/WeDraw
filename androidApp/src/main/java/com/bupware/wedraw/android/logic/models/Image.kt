package com.bupware.wedraw.android.logic.models

import java.sql.Blob

data class Image(
    var id:Long?,
    val byteArray: ByteArray,
): java.io.Serializable