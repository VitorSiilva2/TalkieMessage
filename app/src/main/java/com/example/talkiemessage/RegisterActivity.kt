package com.example.talkiemessage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.talkiemessage.databinding.ActivityRegisterBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = currentUser?.uid

        if (uid != null) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(uid)
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val nameCurrent = document.data?.get("name") as String?

                        var isAdmin = false
                        binding.isadminValidation.setOnCheckedChangeListener { _, isChecked ->
                            isAdmin = isChecked
                        }

                        val auth = FirebaseAuth.getInstance()

                        binding.registerBtnEnter.setOnClickListener {
                            val email = binding.registerEditEmail.text.toString()
                            val password = binding.registerEditPassword.text.toString()
                            val name = binding.registerEditName.text.toString()


                            if (password.isEmpty() || name.isEmpty() || email.isEmpty()) {
                                Toast.makeText(this, "Complete o formulario!", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnSuccessListener { cadastro ->
                                        val uid = cadastro.user?.uid
                                        if (uid == null) {
                                            Toast.makeText(
                                                this,
                                                "Erro interno no servidor",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {

                                            FirebaseFirestore.getInstance()
                                                .collection("/users")
                                                .document(uid)
                                                .set(
                                                    hashMapOf(
                                                        "name" to name,
                                                        "email" to email,
                                                        "uuid" to uid,
                                                        "isAdmin" to isAdmin,
                                                        "affiliated" to nameCurrent
                                                    )
                                                )

                                            binding.registerEditEmail.setText("")
                                            binding.registerEditName.setText("")
                                            binding.registerEditPassword.setText("")

                                        }
                                    }.addOnFailureListener { exception ->
                                        val messageError = when (exception) {
                                            is FirebaseAuthUserCollisionException -> "Conta já cadastrada"
                                            is FirebaseNetworkException -> "Verifique sua conexão com a internet "
                                            else -> "Erro ao cadastrar usuario"
                                        }
                                        Toast.makeText(this, messageError, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    .addOnCompleteListener {
                                        Toast.makeText(
                                            this,
                                            "Conta criando com succeso!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                    }
                }
        }

    }
}