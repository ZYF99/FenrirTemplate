package com.fenrir.fenrirtemplate.manager

import com.fenrir.fenrirtemplate.model.sharedpref.SharedPrefModel


object TokenManager {
    var accessToken: String = SharedPrefModel.accessToken
}