package com.fenrir.fenrirtemplate.ui.base

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import io.reactivex.Single

class UIErrorLiveEvent : LiveEvent<Event<UIException>>()

fun <T> Single<T>.catchUIError(liveEvent: UIErrorLiveEvent): Single<T> = doOnError {
    if (it is UIException) {
        liveEvent.value = Event(it)
    }
}

fun UIErrorLiveEvent.bindDialog(context: Context, owner: LifecycleOwner) {
    observe(owner) { event ->
        if (event.hasBeenHandled) return@observe
        val error = event.peekContent()
        AlertDialog.Builder(context)
            .setTitle(
                context.getString(error.errorTitle)
            )
            .setMessage(context.getString(error.errorReason))
            .setPositiveButton("ok", null)
            .setOnDismissListener { event.getContentIfNotHandled() }
            .show()
    }
}
