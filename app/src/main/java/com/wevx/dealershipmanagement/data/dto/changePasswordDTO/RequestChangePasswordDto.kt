package com.wevx.dealershipmanagement.data.dto.changePasswordDTO

import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
@Serializable
data class RequestChangePasswordDto(
    @SerializedName("newPassword")
    val newPassword: String? = null,
    @SerializedName("oldPassword")
    val oldPassword: String? = null
)