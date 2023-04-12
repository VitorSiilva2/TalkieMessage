package com.example.talkiemessage.login.model

interface LoginCallBack {
    fun onSuccess(isAdmin : Boolean)
    fun onFailure(message:String)
    fun onComplete()

}