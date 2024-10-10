package com.bupware.wedraw

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform