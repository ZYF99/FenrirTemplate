package com.fenrir.fenrirtemplate.model.api.user


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class TokenInfo(
    @Json(name = "expires_in")
    val expiresIn: Int,
    @Json(name = "mail_address")
    val mailAddress: String,
    @Json(name = "scope")
    val scope: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "user_id")
    val userId: Int
)