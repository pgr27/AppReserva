package com.example.peluditosfelices

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.peluditosfelices.modelo.Cita
import com.example.peluditosfelices.viewadapters.CitasAdapter
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PantallaVerCitas : AppCompatActivity() {

    private lateinit var editTextDNI: EditText
    private lateinit var buttonConsultar: Button
    private lateinit var recyclerViewCitas: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_ver_citas)

        // Crear un OnBackPressedCallback y registrarlo para controlar la acción del botón de volver
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@PantallaVerCitas, Pantalla_Menu::class.java)
                startActivity(intent)
                finish()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

        // Inicialización de vistas
        editTextDNI = findViewById(R.id.editTextDNI)
        buttonConsultar = findViewById(R.id.buttonConsultar)
        recyclerViewCitas = findViewById(R.id.recyclerViewCitas)

        // Configurar RecyclerView
        recyclerViewCitas.layoutManager = LinearLayoutManager(this)

        // Configurar botón de consulta
        buttonConsultar.setOnClickListener {
            val dni = editTextDNI.text.toString().trim()
            if (dni.isNotEmpty()) {
                consultarCitas(dni)
            } else {
                Toast.makeText(this, "Por favor, introduce un DNI válido", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Botón "Flecha" para volver al menú
        val backButton = findViewById<ImageView>(R.id.ButtonVolver)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Llama al método para regresar al menú
        }
    }

    private fun consultarCitas(dni: String) {
        // Consultar Firestore
        FirestoreDBConnector.leerCitasParaUsuario(dni) { result, error ->
            if (error != null) {
                Toast.makeText(this, "Error al consultar las citas", Toast.LENGTH_SHORT).show()
            } else if (result != null && !result.isEmpty) {
                mostrarCitas(result)
            } else {
                Toast.makeText(this, "No se encontraron citas para este DNI", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun mostrarCitas(result: QuerySnapshot) {
        val listaCitas = result.map { document ->
            document.toObject(Cita::class.java)
        }

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        // Ordenamos  las citas por fechas(DESCENDENTE)
        val listaCitasOrdenada = listaCitas.sortedByDescending {
            formatter.parse("${it.dia} ${it.hora}")
        }

        val adapter = CitasAdapter(
            listaCitasOrdenada,
            { cita ->
                // Acción al hacer clic en una cita
                Toast.makeText(this, "Cita seleccionada: ${cita.idCita}", Toast.LENGTH_SHORT).show()
            },
            { cita, textView ->
                // Evaluar cada cita para cambiar dinámicamente el color del texto
                evaluarCita(cita, textView)
            }
        )

        recyclerViewCitas.adapter = adapter
    }

    private fun evaluarCita(cita: Cita, view: View) {
        try {
            val formatoFecha =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Formato para fechas
            val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault()) // Formato para horas

            // Obtener fecha y hora actuales
            val fechaActual = formatoFecha.format(Date())
            val horaActual = formatoHora.format(Date())

            val fechaCita = cita.dia.orEmpty() // Fecha de la cita
            val horaCita = cita.hora.orEmpty() // Hora de la cita

            if (fechaCita.isEmpty() || horaCita.isEmpty()) {
                view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
                return
            }

            // Comparar fechas
            when {
                fechaCita < fechaActual -> {
                    view.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            android.R.color.holo_red_light
                        )
                    ) // Pasada
                }

                fechaCita > fechaActual -> {
                    view.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            android.R.color.holo_green_light
                        )
                    ) // Futura
                }

                else -> { // Mismo día, comparar horas
                    val horaCitaDate = formatoHora.parse(horaCita)
                    val horaActualDate = formatoHora.parse(horaActual)

                    if (horaCitaDate != null && horaActualDate != null) {
                        if (horaCitaDate.before(horaActualDate)) {
                            view.setBackgroundColor(
                                ContextCompat.getColor(
                                    this,
                                    android.R.color.holo_red_light
                                )
                            ) // Pasada
                        } else {
                            view.setBackgroundColor(
                                ContextCompat.getColor(
                                    this,
                                    android.R.color.holo_green_light
                                )
                            ) // Futura
                        }
                    } else {
                        // Si algo falla, mostrar en rojo
                        view.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                android.R.color.holo_red_light
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("evaluarCita", "Error evaluando la cita: ${e.message}")
            view.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.holo_red_light
                )
            )
        }
    }
}
