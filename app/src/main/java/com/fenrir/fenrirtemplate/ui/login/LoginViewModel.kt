package com.fenrir.fenrirtemplate.ui.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.fenrir.fenrirtemplate.R
import com.fenrir.fenrirtemplate.manager.TokenManager
import com.fenrir.fenrirtemplate.manager.api.service.UserService
import com.fenrir.fenrirtemplate.model.sharedpref.SharedPrefModel
import com.fenrir.fenrirtemplate.ui.base.BaseViewModel
import com.fenrir.fenrirtemplate.ui.base.UIException
import com.fenrir.fenrirtemplate.util.switchThread
import com.fenrir.fenrirtemplate.util.toJson
import io.reactivex.Single
import org.kodein.di.generic.instance


class LoginViewModel(application: Application) : BaseViewModel(application) {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginSuccess = MutableLiveData<Boolean>()
    val showPassword = MutableLiveData<Boolean>()

    private val userService by instance<UserService>()

    fun login() =
        Single.fromCallable {
            judgeInputParam()
        }.flatMap {
            userService.login(username = email.value ?: "", password = password.value ?: "")
        }.flatMap { authorize ->
            SharedPrefModel.accessToken = authorize.accessToken
            TokenManager.accessToken = authorize.accessToken
            userService.getTokenInfo()
        }
            .autoProgressDialog()
            .switchThread()
            .catchApiError()
            .catchUIError()
            .doOnSuccess { tokenInfo ->
                SharedPrefModel.tokenInfo = tokenInfo.toJson()
                SharedPrefModel.email = email.value ?: ""
                loginSuccess.value = true
            }
            .bindLife()

    fun togglePasswordType() {
        showPassword.value = !(showPassword.value ?: false)
    }

    private fun judgeInputParam() {
        val errorMessage = when {
            email.value.isNullOrEmpty() -> R.string.alert_message_no_address
            password.value.isNullOrEmpty() -> R.string.alert_message_no_password
            else -> -1
        }
        if (errorMessage != -1) {
            throw UIException(
                R.string.common_message,
                errorMessage
            )
        }
    }


}
