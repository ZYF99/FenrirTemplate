package com.fenrir.fenrirtemplate

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.multidex.MultiDex
import com.chibatching.kotpref.Kotpref
import com.facebook.stetho.Stetho
import com.fenrir.fenrirtemplate.manager.di.apiModule
import com.fenrir.fenrirtemplate.manager.di.databaseModule
import com.fenrir.fenrirtemplate.manager.di.tokenModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import timber.log.Timber


open class MainApplication : Application(), KodeinAware {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@MainApplication))
        import(databaseModule(this@MainApplication))
        import(apiModule)
        import(tokenModule)
        /* bindings */
    }

    override fun onCreate() {
        super.onCreate()

        Kotpref.init(this)
        if (BuildConfig.DEBUG && Build.DEVICE != "robolectric") {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}