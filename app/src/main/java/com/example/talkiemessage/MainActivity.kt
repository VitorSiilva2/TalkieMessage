package com.example.talkiemessage

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.talkiemessage.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtnEnter.setOnClickListener{
            val email = binding.loginEditInput.text.toString()
            val password = binding.passwordEditInput.text.toString()

            if (validate()) {
                Toast.makeText(this, "Preencha os campos", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authentication ->
                if(authentication.isSuccessful) {
                    mainScreen()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }

        }

        }

        binding.registerTextEnter.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validate(): Boolean {
        return binding.loginEditInput.text.isEmpty() ||
                binding.passwordEditInput.text.isEmpty()
    }

    private fun mainScreen(){

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val uid = user.uid
            val db = FirebaseFirestore.getInstance()
            val usersRef = db.collection("users").document(uid)

            usersRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists() && document.getBoolean("isAdmin") == true) {
                        // O usuário é um admin, vá para a atividade de admin
                        val intent = Intent(this@MainActivity, AdminScreen::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // O usuário não é um admin, vá para a atividade de usuário normal
                        val intent = Intent(this@MainActivity, ClientScreen::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Erro na leitura dos dados", exception)
                }
        } else {
            Log.e(TAG, "Não autenticado")
        }
    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser // user atual o currentUser

        if(user != null) {
            mainScreen()
        }
    }

}