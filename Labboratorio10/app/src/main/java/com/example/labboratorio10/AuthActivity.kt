package com.example.labboratorio10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {
    val button = findViewById<Button>(R.id.button)
    val button2 = findViewById<Button>(R.id.button2)
    val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
    val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     

        setContentView(R.layout.activity_auth)
        setup()
    }

    private fun setup(){
        title="Autenticación"
        button.setOnClickListener{
            if(editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(editTextEmail.text.toString(),editTextPassword.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    }
                    else{
                        showAlert()
                    }
                }
            }
        }
        button2.setOnClickListener{
            if(editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(editTextEmail.text.toString(),editTextPassword.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    }
                    else{
                        showAlert()
                    }
                }
            }
        }

    }
    private fun showAlert(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email:String, provider: ProviderType){
        val homeIntent: Intent = Intent(this, HomeActivity::class.java).apply{
            putExtra("email",email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}