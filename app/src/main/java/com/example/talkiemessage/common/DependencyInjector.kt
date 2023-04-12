package com.example.talkiemessage.common

import com.example.talkiemessage.login.model.FireLoginDataSource
import com.example.talkiemessage.login.model.LoginDataSource
import com.example.talkiemessage.login.model.LoginRepository

object DependencyInjector {

    fun loginRepository() : LoginRepository {
        return LoginRepository(FireLoginDataSource())
    }

}