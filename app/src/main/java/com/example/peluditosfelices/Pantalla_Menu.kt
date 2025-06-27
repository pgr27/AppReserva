package com.example.peluditosfelices

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class Pantalla_Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_menu)
        // Botón "Reservar"
        val btnReservar = findViewById<Button>(R.id.ButtonReservar)
        btnReservar.setOnClickListener {
            val intent = Intent(this, pantallaServicios::class.java)
            startActivity(intent)
        }
        // Botón "Mis citas"
        val btnMisCitas = findViewById<Button>(R.id.ButtonMisCitas)
        btnMisCitas.setOnClickListener {
            val intent = Intent(this, PantallaVerCitas::class.java)
            startActivity(intent)
        }
        // Botón "Ofertas"
        val btnOfertas = findViewById<Button>(R.id.ButtonOfertas)
        btnOfertas.setOnClickListener {
            val intent = Intent(this, PantallaOfertas::class.java)
            startActivity(intent)
        }
        // Botón "Contacto"
        val btnContacto = findViewById<Button>(R.id.ButtonContacto)
        btnContacto.setOnClickListener {
            val intent = Intent(this, PantallaContacto::class.java)
            startActivity(intent)
        }
        // OnBackPressedCallback para controlar la acción del botón de volver
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }


}