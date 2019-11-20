package com.fenrir.fenrirtemplate.util

import android.content.Context
import android.text.TextUtils
import androidx.annotation.StringRes
import com.fenrir.fenrirtemplate.R
import io.reactivex.*
import org.reactivestreams.Publisher

class HandleErrorTransform<T>(private val context: Context) : ObservableTransformer<T, T>,
    FlowableTransformer<T, T>,
    MaybeTransformer<T, T>,
    SingleTransformer<T, T>,
    CompletableTransformer {

    override fun apply(upstream: Observable<T>): ObservableSource<T> =
        upstream.toFlowable(BackpressureStrategy.BUFFER).retryWhen {
            it.compose(handleErrorFlow(context))
        }
            .toObservable()

    override fun apply(upstream: Completable): CompletableSource =
        upstream.retryWhen { it.compose(handleErrorFlow(context)) }

    override fun apply(upstream: Flowable<T>): Publisher<T> =
        upstream.retryWhen { it.compose(handleErrorFlow(context)) }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> =
        upstream.retryWhen { it.compose(handleErrorFlow(context)) }

    override fun apply(upstream: Single<T>): SingleSource<T> =
        upstream.retryWhen { it.compose(handleErrorFlow(context)) }
}


private fun handleErrorFlow(context: Context): FlowableTransformer<Throwable, Unit> {
    return FlowableTransformer { f ->
        f.flatMap { t ->
            when (t) {
                is AppFinishException -> {
                    //finish app
                    Flowable.error(t)
                }
                else -> {
                    showErrorDialogSingle(context = context, throwable = t)
                        .toFlowable()
                        .flatMap { event ->
                            when (event.button) {
                                DialogUtil.BUTTON_TYPE_RETRY -> Flowable.just(Unit)
                                DialogUtil.BUTTON_TYPE_CANCEL,
                                DialogUtil.BUTTON_TYPE_CLOSE,
                                DialogUtil.BUTTON_TYPE_DISMISS -> Flowable.error(t)
                                else -> Flowable.error(t)
                            }
                        }
                }
            }
        }

    }
}


fun <T> autoHandleError(context: Context): HandleErrorTransform<T> =
    HandleErrorTransform(context)

private fun showErrorDialogSingle(
    throwable: Throwable,
    context: Context,
    title: String = "",
    msg: String = "",
    @StringRes positiveButtonText: Int = R.string.dialog_ok,
    @StringRes negativeButtonText: Int = R.string.dialog_cancel,
    @DialogUtil.ButtonType positiveButton: Long = DialogUtil.BUTTON_TYPE_OK,
    @DialogUtil.ButtonType negativeButton: Long = DialogUtil.BUTTON_TYPE_CANCEL
): Single<DialogUtil.DialogEvent> {
    val text =
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(msg)) title to msg
        else ApiUtil.parseError2Text(throwable)
    return DialogUtil.showDialogSingle(
        context,
        text.first,
        text.second,
        positiveButtonText,
        negativeButtonText,
        positiveButton,
        negativeButton
    )
}

// define the API error response model inside
class ServerErrorException : RuntimeException()

class AppFinishException : RuntimeException()



