package com.example.labboratorio10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{
    BASIC
}

class HomeActivity : AppCompatActivity() {
    val button = findViewById<Button>(R.id.button)
    val emailView = findViewById<TextView>(R.id.textView2)
    val providerView = findViewById<TextView>(R.id.textView)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bundle: Bundle?= intent.extras
        val email=bundle?.getString("email")
        val provider=bundle?.getString("provider")
        setup(email?:"",provider?:"")
    }
    private fun setup(email:String,provider:String){
        title="Inicio"
        emailView.text=email
        providerView.text=provider
        button.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
}