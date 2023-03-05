package com.example.talkiemessage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.talkiemessage.databinding.ActivityClientScreenBinding
import com.google.firebase.auth.FirebaseAuth

class ClientScreen : AppCompatActivity() {

    private lateinit var binding : ActivityClientScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.singinBtnLogoff.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val backToLogin = Intent(this, MainActivity::class.java)
            startActivity(backToLogin)
        }
    }
}