package com.fenrir.fenrirtemplate.model.sharedpref

import com.chibatching.kotpref.KotprefModel
import com.fenrir.fenrirtemplate.util.Constants

object SharedPrefModel : KotprefModel() {
    override val kotprefName: String = Constants.SHARED_PREF_FILE_NAME
    var accessToken: String by stringPref()
    var tokenInfo : String by stringPref()
    var email : String by stringPref()

}