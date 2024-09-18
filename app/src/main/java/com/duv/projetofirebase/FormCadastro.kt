package com.duv.projetofirebase

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.duv.projetofirebase.databinding.ActivityFormCadastroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class FormCadastro : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var usuarioID: String
    private lateinit var binding: ActivityFormCadastroBinding
    val message = listOf("Todos os campos devem ser preechidos", "Cadastro realizado com sucesso")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        binding.btCadastrar.setOnClickListener { view ->
            val name = binding.editName.text.toString()
            val email = binding.editEmailCadastro.text.toString()
            val senha = binding.editSenhaCadastro.text.toString()

            if (name.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                val snackbar = Snackbar.make(view, message[0], Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else{
                CadastrarUsuario(view)
            }
        }
    }

    private fun SalvarDadosUsuario(){
        val name = binding.editName.text.toString()
        val email = binding.editEmailCadastro.text.toString()
        val map = hashMapOf(
            "nome" to name,
            "email" to email,
        )

        usuarioID = auth.currentUser!!.uid

        val documentReference = db.collection("Usuários").document(usuarioID)
        documentReference.set(map).addOnSuccessListener {
            Log.d("db","sucesso ao salvar os dados")
        }.addOnFailureListener {
            Log.d("db_error", "erro ao salvar os dados $it")
        }
    }

    private fun  CadastrarUsuario(view: View) {
        val email = binding.editEmailCadastro.text.toString()
        val senha = binding.editSenhaCadastro.text.toString()

        auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener {
          task ->

          if(task.isSuccessful) {

              SalvarDadosUsuario()
              binding.editEmailCadastro.setText("")
              binding.editSenhaCadastro.setText("")
              binding.editName.setText("")

              val snackbar = Snackbar.make(view, message[1], Snackbar.LENGTH_SHORT)
              snackbar.setBackgroundTint(Color.BLUE)
              snackbar.show()
          }else {
              var erro : String
              try {
                throw task.exception!!
              } catch (e: FirebaseAuthWeakPasswordException) {
                erro = "Digite no minimo 6 caracteres"
              }catch (e:FirebaseAuthUserCollisionException){
                  erro = "Essa conta ja foi cadastrada"
              }catch (e:FirebaseAuthInvalidCredentialsException){
                  erro = "Digite um email válido"
              }
          catch (e: Exception){
                erro = "Ocorreu um erro inesperado"
              }
              val snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT)
              snackbar.setBackgroundTint(Color.RED)
              snackbar.show()
          }
        }
    }
}