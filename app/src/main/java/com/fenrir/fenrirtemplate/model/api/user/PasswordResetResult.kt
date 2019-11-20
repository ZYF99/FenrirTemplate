package com.fenrir.fenrirtemplate.model.api.user

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PasswordResetResult(
    val status: String,
    val description: String
)