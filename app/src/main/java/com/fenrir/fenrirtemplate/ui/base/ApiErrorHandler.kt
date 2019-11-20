package com.fenrir.fenrirtemplate.ui.base

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.fenrir.fenrirtemplate.manager.api.base.ApiException
import io.reactivex.Single

class ApiErrorLiveEvent : LiveEvent<Event<ApiException>>()

fun <T> Single<T>.catchApiError(liveEvent: ApiErrorLiveEvent): Single<T> = doOnError {
    if (it is ApiException) {
        liveEvent.value = Event(it)
    }
}

fun ApiErrorLiveEvent.bindDialog(context: Context, owner: LifecycleOwner) {
    observe(owner) { event ->
        if (event.hasBeenHandled) return@observe
        val error = event.peekContent()
        AlertDialog.Builder(context)
            .setTitle(error.error)
            .setMessage(error.errorDescription)
            .setPositiveButton("ok", null)
            .setOnDismissListener { event.getContentIfNotHandled() }
            .show()
    }
}
