package com.fenrir.fenrirtemplate.manager.api.base

import com.fenrir.fenrirtemplate.util.fromJson
import okhttp3.Interceptor
import okhttp3.Response


class NetErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code < 200 || response.code >= 300) {
            response.body?.string()?.fromJson<ApiException>()?.also {
                throw it
            }
        }

        return response
    }
}


