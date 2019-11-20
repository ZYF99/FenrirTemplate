package com.fenrir.fenrirtemplate.manager.di

import android.app.Application
import androidx.room.Room
import com.fenrir.fenrirtemplate.BuildConfig
import com.fenrir.fenrirtemplate.manager.TokenManager
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import com.fenrir.fenrirtemplate.manager.api.base.ApiClient
import com.fenrir.fenrirtemplate.manager.api.base.HeaderInterceptor
import com.fenrir.fenrirtemplate.manager.api.base.NetErrorInterceptor
import com.fenrir.fenrirtemplate.manager.api.service.UserService
import com.fenrir.fenrirtemplate.model.db.AppDatabase
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


val apiModule = Kodein.Module("api") {
    bind<ApiClient>() with singleton { provideApiClient() }
    bind<UserService>() with singleton { instance<ApiClient>().createService(UserService::class.java) }

}

val tokenModule = Kodein.Module("tokenManager") {
    bind<TokenManager>() with singleton { TokenManager }
}

fun databaseModule(app: Application) = Kodein.Module("database") {
    bind<AppDatabase>() with singleton {
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME_APP
        ).build()
    }
}


fun provideApiClient(): ApiClient {
    val client = ApiClient.Builder()
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    client.okBuilder
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(NetErrorInterceptor())
        .readTimeout(30, TimeUnit.SECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }

    return client.build(baseUrl = BuildConfig.BASE_URL)
}