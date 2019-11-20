package com.fenrir.fenrirtemplate.manager.api.service

import com.fenrir.fenrirtemplate.model.api.user.Authorize
import com.fenrir.fenrirtemplate.model.api.user.Registration
import com.fenrir.fenrirtemplate.model.api.user.TokenInfo
import com.fenrir.fenrirtemplate.util.Constants
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {


    /// API: POST [/auth/authorize][URL]
    ///
    /// [URL]:
    @FormUrlEncoded
    @POST("auth/authorize?grant_type=password&scope=use")
    fun login(
        @Field("client_id") clientId: String = Constants.CLIENT_ID,
        @Field("client_secret") clientSecret: String = Constants.CLIENT_SECRET,
        @Field("username") username: String,
        @Field("password") password: String
    ): Single<Authorize>


    /// API: POST [/api/user/create][URL]
    ///
    /// [URL]:
    @FormUrlEncoded
    @POST("api/user/create")
    fun registerNewUser(
        @Field("mail_address") email: String,
        @Field("password") password: String
    ): Single<Registration>


    /// API: GET [/auth/tokeninfo][URL]
    ///
    /// [URL]:
    @GET("auth/tokeninfo")
    fun getTokenInfo(): Single<TokenInfo>


}