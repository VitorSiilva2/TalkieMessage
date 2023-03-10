package com.example.talkiemessage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.talkiemessage.databinding.ActivityAdminScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AdminScreen : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val uid = currentUser?.uid
    private lateinit var binding : ActivityAdminScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (uid != null) {
            val userRef = db.collection("users").document(uid)
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.data?.get("name") as String?
                        binding.welcomeText.text = "Welcome, $name"
                    } else {
                        Log.d("AdminScreen", "Documento não encontrado")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("AdminScreen", "Erro ao recuperar dados do usuário: ", exception)
                }
        }

        // Obtém a referência para a coleção de usuários
        val usersRef = db.collection("users")

        // Define o evento de clique do botão de enviar mensagem
        binding.sendBtnPerson.setOnClickListener {
            // Obtém o ID do usuário destinatário e a mensagem a ser enviada
            val receiverId = binding.personTextWrite.text.toString()
            val message = binding.writeText.text.toString()

            // Verifica se o ID do usuário destinatário é válido
            if (receiverId.isEmpty()) {
                Toast.makeText(this, "Please enter a receiver ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifica se a mensagem é válida
            if (message.isEmpty()) {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cria um novo documento na subcoleção "messages" do usuário remetente
            val senderRef = usersRef.document(receiverId)
            val messageData = hashMapOf(
                "senderId" to uid,
                "receiverId" to receiverId,
                "message" to message,
                "timestamp" to Calendar.getInstance().timeInMillis
            )
            senderRef.collection("messages").add(messageData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
                    binding.personTextWrite.text.clear()
                    binding.writeText.text.clear()
                }
                .addOnFailureListener { exception ->
                    Log.d("AdminScreen", "Error sending message", exception)
                    Toast.makeText(this, "Error sending message", Toast.LENGTH_SHORT).show()
                }
        }

        // Define o evento de clique do botão de sair
        binding.singinBtnLogoff.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val backToLogin = Intent(this, MainActivity::class.java)
            startActivity(backToLogin)
        }
    }
}