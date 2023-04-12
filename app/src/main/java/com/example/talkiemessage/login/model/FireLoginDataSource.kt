package com.example.talkiemessage.login.model

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FireLoginDataSource : LoginDataSource {

    override fun login(email: String, password: String, callBack: LoginCallBack) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authentication ->
                if (authentication.user != null) {
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        val uid = user.uid
                        val db = FirebaseFirestore.getInstance()
                        val usersRef = db.collection("users").document(uid)

                        usersRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null && document.exists() && document.getBoolean("isAdmin") == true) {
                                    callBack.onSuccess(true)
                                } else {
                                    callBack.onSuccess(false)
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e(TAG, "Erro na leitura dos dados", exception)
                            }
                    } else {
                        Log.e(TAG, "Não autenticado")
                    }
                } else {
                    callBack.onFailure("Usuario Não encontrado")
                }
            }
            .addOnFailureListener {exception ->
                callBack.onFailure(exception.message ?: "Erro ao fazer login")
            }
            .addOnCompleteListener{
                callBack.onComplete()
            }
    }
}