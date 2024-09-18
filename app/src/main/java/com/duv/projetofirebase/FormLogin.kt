package com.duv.projetofirebase

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.duv.projetofirebase.databinding.ActivityFormLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.android.HandlerDispatcher

class FormLogin : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityFormLoginBinding
    val message = listOf("Todos os campos devem ser preechidos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        binding.txtTelaCadastro.setOnClickListener {
            val intent = Intent(this, FormCadastro::class.java)
            startActivity(intent)
        }

        binding.btEntrar.setOnClickListener {view ->
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            if (email.isEmpty() || senha.isEmpty()) {
                val snackbar = Snackbar.make(view, message[0], Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                AutenticarUsuario(view)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val usuarioAtual = auth.currentUser

        if (usuarioAtual != null) {
            telaPerfil()
        }
    }
    private fun telaPerfil(){
        val intent = Intent(this, TelaPerfil::class.java)
        startActivity(intent)
        finish()
    }
    private fun AutenticarUsuario(view:View) {
        val email = binding.editEmail.text.toString()
        val senha = binding.editSenha.text.toString()
        auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener {
            task ->
            if (task.isSuccessful) {
                binding.progressBar.visibility = View.VISIBLE
                Handler().postDelayed( {
                    telaPerfil()
                },3000)
            }
        }.addOnFailureListener {e ->
            val snackbar = Snackbar.make(view, "erro ao logar", Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()

        }
    }
}