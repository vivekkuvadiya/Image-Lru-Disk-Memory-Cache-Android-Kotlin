package com.example.imagelrucache.utils

import android.util.Base64
import java.nio.charset.StandardCharsets

fun String.encodeToBase64(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.DEFAULT).toString()
}

fun String.decodeFromBase64(): String {
    return String(Base64.decode(this, Base64.DEFAULT), StandardCharsets.UTF_8)
}