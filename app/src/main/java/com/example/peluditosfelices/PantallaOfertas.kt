package com.example.peluditosfelices
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.peluditosfelices.modelo.Oferta
import com.example.peluditosfelices.viewadapters.OfertaAdapter

class PantallaOfertas : AppCompatActivity() {

    private lateinit var recyclerViewOfertas: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_ofertas)

        // Inicializar el RecyclerView
        recyclerViewOfertas = findViewById(R.id.recyclerViewOfertas)
        recyclerViewOfertas.layoutManager = LinearLayoutManager(this)

        //  OnBackPressedCallback para controlar la acción del botón de volver
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@PantallaOfertas, Pantalla_Menu::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

        // Configurar las ofertas
        cargarOfertas()

        // Botón "Flecha" (volver a la ventana anterior)
        val backButton = findViewById<ImageView>(R.id.ButtonVolver)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    // Método para cargar ofertas y asignarlas al RecyclerView
    private fun cargarOfertas() {
        // Usar el método leerDatos para obtener las ofertas
        FirestoreDBConnector.leerDatos(
            collectionPath = "Ofertas",
            onSuccess = { datos ->
                val listaOfertas = datos.map { ofertaData ->
                    Oferta(
                        Titulo = ofertaData["Titulo"] as? String ?: "",
                        Descripcion = ofertaData["Descripcion"] as? String ?: "",
                        Duracion = ofertaData["Duracion"] as? String ?: ""
                    )
                }

                // Configurar el adaptador con las ofertas obtenidas
                val adapter = OfertaAdapter(listaOfertas)
                recyclerViewOfertas.adapter = adapter

                // Log de confirmación
                Log.d("PantallaOfertas", "Adapter configurado con: $listaOfertas")
            },
            onFailure = { exception ->
                // Manejo de errores
                Log.e("PantallaOfertas", "Error al cargar las ofertas", exception)
                Toast.makeText(this, "Error al cargar las ofertas", Toast.LENGTH_SHORT).show()
            }
        )
    }

}
