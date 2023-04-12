package com.example.talkiemessage.login.view

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.talkiemessage.ClientScreen
import com.example.talkiemessage.login.presenter.LoginPresenter
import com.example.talkiemessage.admin.view.AdminScreen
import com.example.talkiemessage.common.DependencyInjector
import com.example.talkiemessage.common.TxtWatcher
import com.example.talkiemessage.databinding.ActivityLoginBinding
import com.example.talkiemessage.login.presenter.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity(), Login.View {

    private lateinit var binding: ActivityLoginBinding

    override lateinit var presenter: Login.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = LoginPresenter(this, DependencyInjector.loginRepository())

        with(binding) {
            loginEditName.addTextChangedListener(watcher)
            loginEditName.addTextChangedListener(TxtWatcher {
                displayEmailFailure(null)
            })

            loginEditPassword.addTextChangedListener(watcher)
            loginEditPassword.addTextChangedListener(TxtWatcher {
                displayPasswordFailure(null)
            })

            loginBtnEnter.setOnClickListener {
                presenter.login(loginEditName.text.toString(), loginEditPassword.text.toString())
            }
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private val watcher = TxtWatcher {
        binding.loginBtnEnter.isEnabled = binding.loginEditName.text.toString().isNotEmpty()
                && binding.loginEditPassword.text.toString().isNotEmpty()
    }

    override fun showProgress(enabled: Boolean) {
    }

    override fun displayEmailFailure(emailError: Int?) {
        binding.loginEditNameInput.error = emailError?.let { getString(it) }
    }

    override fun displayPasswordFailure(passwordError: Int?) {
        binding.loginEditPasswordInput.error = passwordError?.let { getString(it) }
    }

    override fun onUserAuthenticated(isAdmin : Boolean) {
        val intent = if (isAdmin) {
            Intent(this, AdminScreen::class.java)
        } else {
            Intent(this, ClientScreen::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onUserUnauthorized(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}