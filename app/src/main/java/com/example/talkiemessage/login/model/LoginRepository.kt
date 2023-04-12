package com.example.talkiemessage.login.model

class LoginRepository (private val dataSource: LoginDataSource) {

    fun login(email: String, password: String, callback : LoginCallBack) {
        dataSource.login(email, password, callback)
    }

}