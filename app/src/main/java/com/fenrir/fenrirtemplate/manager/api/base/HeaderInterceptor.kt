package com.fenrir.fenrirtemplate.manager.api.base

import com.fenrir.fenrirtemplate.model.sharedpref.SharedPrefModel
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {



    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            //TODO add header logic
            .header("Authorization", "Bearer ${SharedPrefModel.accessToken}")
            .build()

        return chain.proceed(request)
    }

}