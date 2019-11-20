package com.fenrir.fenrirtemplate.ui.base

import android.app.Application
import androidx.lifecycle.*
import com.fenrir.fenrirtemplate.util.BindLife
import com.fenrir.fenrirtemplate.util.autoProgressDialog
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein


abstract class BaseViewModel(app: Application) : AndroidViewModel(app),
    LifecycleObserver,
    BindLife,
    KodeinAware {

    override val kodein by kodein()

    override val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    val apiError = ApiErrorLiveEvent()
    val uiError = UIErrorLiveEvent()

    val progressDialog = MutableLiveData<Boolean>().default(false)

    protected fun <T> Single<T>.catchApiError(): Single<T> = catchApiError(apiError)
    protected fun <T> Single<T>.catchUIError(): Single<T> = catchUIError(uiError)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun <T> Single<T>.autoProgressDialog(): Single<T> = autoProgressDialog(progressDialog)

    fun <T> MutableLiveData<T>.default(value: T) = this.apply { this.value = value }


}