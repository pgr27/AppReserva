package com.example.peluditosfelices

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PantallaInicio : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_inicio)

        val btnAcceder = findViewById<Button>(R.id.ButtonAcceder)

        // Configurar el clic en el botón
        btnAcceder.setOnClickListener {
            val intent = Intent(this, Pantalla_Menu::class.java)
            startActivity(intent) // Iniciar la nueva actividad
            finish()
        }
    }
}
