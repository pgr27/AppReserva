package com.example.peluditosfelices

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.peluditosfelices.modelo.Servicio
import com.example.peluditosfelices.viewadapters.ServicioAdapter
import com.google.firebase.firestore.FirebaseFirestore

class pantallaServicios : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance() // Instancia de Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_servicios)

        // Crear un OnBackPressedCallback y registrarlo para controlar la acción del botón de volver
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@pantallaServicios, Pantalla_Menu::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

        // Botón Volver
        findViewById<ImageButton>(R.id.ButtonVolver).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewServicios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        FirestoreDBConnector.obtenerServicios { servicios ->
            recyclerView.adapter = ServicioAdapter(servicios) { servicio ->
                guardarServicioSeleccionado(servicio)
            }
        }
    }

    private fun guardarServicioSeleccionado(servicio: Servicio) {
        val intent = Intent(this, PantallaFyH::class.java).apply {
            putExtra("SERVICIO", servicio.id_nombreServicio)
            putExtra("PRECIO", servicio.precio)
            putExtra("DURACION", servicio.duracion)
        }
        startActivity(intent)
    }

}
