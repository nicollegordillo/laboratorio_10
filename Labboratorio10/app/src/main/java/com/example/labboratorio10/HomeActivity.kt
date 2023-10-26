package com.example.labboratorio10

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    BASIC,
    GOOGLE
}

class HomeActivity : AppCompatActivity() {
    private lateinit var button :Button
    private lateinit var emailView :TextView
    private lateinit var providerView:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

         button = findViewById(R.id.button)
         emailView = findViewById(R.id.textView2)
        providerView = findViewById(R.id.textView)

        val bundle: Bundle? = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?: "", provider ?: "")

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("email", email)
        editor.putString("provider", provider)
        editor.apply()
    }

    private fun setup(email: String, provider: String) {
        title = "Inicio"
        emailView.text = email
        providerView.text = provider
        button.setOnClickListener {
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.clear()
            editor.apply()
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
}