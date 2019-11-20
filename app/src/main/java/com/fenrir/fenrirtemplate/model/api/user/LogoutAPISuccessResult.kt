package com.fenrir.fenrirtemplate.model.api.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogoutAPISuccessResult(val status: String)