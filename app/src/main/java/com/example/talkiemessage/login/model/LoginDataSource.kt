package com.example.talkiemessage.login.model

interface LoginDataSource {
    fun login(email : String, password : String, callBack: LoginCallBack)
}
