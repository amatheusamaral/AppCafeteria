package com.duv.projetofirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.duv.projetofirebase.databinding.ActivityTelaPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaPerfil : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityTelaPerfilBinding
    private val auth = FirebaseAuth.getInstance()
    private lateinit var usuarioID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        binding.deslogar.setOnClickListener {
            auth.signOut()
            val intent = Intent(this,FormLogin::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        usuarioID = auth.currentUser!!.uid
        val documentReference = db.collection("Usuários").document(usuarioID)
        documentReference.addSnapshotListener { value, error ->
            if (value != null) {
                binding.txtNomeUsuario.setText(value.getString("nome"))
                binding.txtEmailUsuario.setText(value.getString("email"))
            }
        }
    }
}