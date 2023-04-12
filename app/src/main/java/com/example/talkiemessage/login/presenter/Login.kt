package com.example.talkiemessage.login.presenter

import androidx.annotation.StringRes
import com.example.talkiemessage.common.BasePresenter
import com.example.talkiemessage.common.BaseView

interface Login {

    interface Presenter : BasePresenter {
        fun login(email: String, password: String)
    }

    interface View : BaseView<Presenter> {
        fun showProgress(enabled: Boolean)
        fun displayEmailFailure(@StringRes emailError: Int?)
        fun displayPasswordFailure(@StringRes passwordError: Int?)
        fun onUserAuthenticated(isAdmin : Boolean)
        fun onUserUnauthorized(message: String)
    }
}