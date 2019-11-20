package com.fenrir.fenrirtemplate.ui.base

import java.lang.Exception

class UIException(
    val errorTitle: Int,
    val errorReason: Int
) : Exception()