package com.example.talkiemessage.login.presenter

import android.util.Patterns
import com.example.talkiemessage.R
import com.example.talkiemessage.login.model.LoginCallBack
import com.example.talkiemessage.login.model.LoginRepository

class LoginPresenter (
    private var view: Login.View?,
    private var repository: LoginRepository) : Login.Presenter {

    override fun login(email: String, password: String) {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid =password.length >=8

        if(!isEmailValid) {
            view?.displayEmailFailure(R.string.invalid_email)
        } else {
            view?.displayEmailFailure(null)
        }

        if (!isPasswordValid) {
            view?.displayEmailFailure(R.string.invalid_password)
        } else {
            view?.displayEmailFailure(null)
        }

if (isEmailValid && isPasswordValid) {
    repository.login(email, password, object : LoginCallBack {

        override fun onSuccess(isAdmin: Boolean) {
            view?.onUserAuthenticated(isAdmin)        }

        override fun onFailure(message: String) {
            view?.onUserUnauthorized(message)
        }

        override fun onComplete() {

        }
    })
}

    }

    override fun onDestroy() {
        view = null
    }
}