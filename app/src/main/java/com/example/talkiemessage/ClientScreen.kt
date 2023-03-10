package com.example.talkiemessage

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import com.example.talkiemessage.databinding.ActivityClientScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class ClientScreen : AppCompatActivity() {

    data class Message(
        val message: String,
    ) {
        constructor() : this("")
    }

    private lateinit var binding : ActivityClientScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(uid)
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.data?.get("name") as String?
                        binding.welcomeText.text = name
                    } else {
                        Log.d(TAG, "Documento não encontrado")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Erro ao recuperar dados do usuário: ", exception)
                }
        }

        binding.confirmedText.setOnClickListener {
        }

        binding.singinBtnLogoff.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val backToLogin = Intent(this, MainActivity::class.java)
            startActivity(backToLogin)

        }

        if (uid != null) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(uid)
            val messagesRef = userRef.collection("messages")

            // Cria um listener para ouvir as mensagens do Firebase
            val messagesListener = object : EventListener<QuerySnapshot> {
                override fun onEvent(snapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                    if (e != null) {
                        Log.w(TAG, "listen:error", e)
                        return
                    }

                    if (snapshot != null) {
                        val messages = snapshot.toObjects(Message::class.java)
                        // Atualiza a UI com as mensagens recebidas
                        updateUI(messages)
                    } else {
                        Log.d(TAG, "Current data: null")
                    }
                }
            }

            // Adiciona o listener às mensagens do Firebase
            messagesRef.addSnapshotListener(messagesListener)
        }

    }

    private fun updateUI(messages: List<Message>) {
        // Encontre a TextView na UI onde as mensagens serão exibidas
        val receivedText = binding.receivedText

        // Recupere a última mensagem da lista de mensagens
        val lastMessage = messages.lastOrNull()

        // Se a última mensagem existir, exiba-a na UI
        if (lastMessage != null) {
            receivedText.text = lastMessage.message
        }
    }
}