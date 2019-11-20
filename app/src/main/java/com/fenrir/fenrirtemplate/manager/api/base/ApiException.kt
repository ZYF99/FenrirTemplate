package com.fenrir.fenrirtemplate.manager.api.base

import com.squareup.moshi.Json
import java.lang.Exception

data class ApiException(
    val error: String,
    @Json(name = "error_description")
    val errorDescription: String,
    val reasons: List<Reason>
) : Exception() {
    data class Reason(
        val reason: String,
        val message: String
    )
}