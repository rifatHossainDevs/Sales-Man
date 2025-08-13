package com.wevx.dealershipmanagement.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun String.toPart(): RequestBody =
    RequestBody.create("text/plain".toMediaTypeOrNull(), this)

fun File.toImagePart(partName: String): MultipartBody.Part {
    val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), this)
    return MultipartBody.Part.createFormData(partName, this.name, requestBody)
}