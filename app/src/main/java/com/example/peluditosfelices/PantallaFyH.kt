package com.example.peluditosfelices

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.peluditosfelices.modelo.Cita
import com.example.peluditosfelices.modelo.Mascota
import com.example.peluditosfelices.modelo.Usuario
import java.text.SimpleDateFormat
import java.util.*

class PantallaFyH : AppCompatActivity() {

    private var fechaSeleccionada: String = ""
    private var horaSeleccionada: String = ""
    private var servicioSeleccionado: String = ""
    private var precioSeleccionado: Double = 0.0
    private var duracionSeleccionada: Int = 0

    private val TAG = "PantallaFyH"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_f_h)

        // Configurar español como idioma predeterminado
        val locale = Locale("es", "ES")
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)

        // Recibir datos de la pantalla anterior
        servicioSeleccionado = intent.getStringExtra("SERVICIO") ?: "Sin servicio"
        precioSeleccionado = intent.getDoubleExtra("PRECIO", 0.0)
        duracionSeleccionada = intent.getIntExtra("DURACION", 0)

        val calendarView = findViewById<CalendarView>(R.id.Calendario)
        val gridHoras = findViewById<GridLayout>(R.id.grid_horas)
        cargarHorasDisponibles(gridHoras, Calendar.getInstance())

        // Crear un OnBackPressedCallback y registrarlo para controlar la acción del botón de volver
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@PantallaFyH, pantallaServicios::class.java)
                startActivity(intent)
                finish() // Cierra la pantalla actual para evitar regresar aquí
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

        // Botón "Flecha" para volver
        findViewById<ImageButton>(R.id.ButtonVolver).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val calendar = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
        }

        // Escuchar cambios en el calendario
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            cargarHorasDisponibles(gridHoras, calendar)
        }
    }

    private fun cargarHorasDisponibles(gridHoras: GridLayout, calendar: Calendar) {
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        fechaSeleccionada = formatoFecha.format(calendar.time)
        gridHoras.removeAllViews()

        Log.d(TAG, "SE OBTIENE EL duración del documento.")
        // Verificar si es domingo
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            mostrarVentanaError("Día no disponible", "No se pueden reservar citas los domingos")
            return // Salir de la función
        }
        if (duracionSeleccionada != 0) {
            FirestoreDBConnector.obtenerHorasPorDiaYDuracion(fechaSeleccionada, duracionSeleccionada) { horasDisponibles, error ->
                if (error != null) {
                    mostrarVentanaError("Error", "Error al consultar las horas disponibles")
                } else if (horasDisponibles != null) {
                    Log.d(TAG, "Horarios generados: $horasDisponibles")
                    horasDisponibles.forEach { hora ->
                        val horaView = TextView(gridHoras.context).apply {
                            text = hora
                            textSize = 18f // Aumentar tamaño del texto
                            setPadding(16, 16, 16, 16) // Espaciado interno
                            minWidth = 200 // Ancho mínimo del botón
                            minHeight = 100 // Alto mínimo del botón
                            gravity = Gravity.CENTER
                            layoutParams = GridLayout.LayoutParams().apply {
                                setMargins(30, 30, 30, 30) // Márgenes entre los botones
                                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f) // Distribución equitativa
                            }
                            setBackgroundResource(R.drawable.estilos_botones)
                            setOnClickListener {
                                horaSeleccionada = hora // Almacenar la hora seleccionada
                                mostrarFormulario()
                            }
                        }
                        gridHoras.addView(horaView)
                    }
                } else {
                    mostrarVentanaError("Error", "No se encontró ninguna hora disponible")
                }
            }
        } else {
            Log.e(TAG, "No se encontró el campo 'Duracion' en el documento del servicio.")
        }
    }



    // Mostrar formulario de reserva
    private fun mostrarFormulario() {
        val dialogView = layoutInflater.inflate(R.layout.pantalla_datos_reserva, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        val textfechaHora = dialogView.findViewById<TextView>(R.id.TextFyH)
        val textServicioPrecio = dialogView.findViewById<TextView>(R.id.TextServicio)
        val btnReservar = dialogView.findViewById<Button>(R.id.ButtonReservarCita)

        // Configurar los datos iniciales en el formulario
        textfechaHora.text = "Día y hora: $fechaSeleccionada $horaSeleccionada"

        textServicioPrecio.text = "Servicio: $servicioSeleccionado $precioSeleccionado €"

        // Configurar el botón para procesar la reserva
        btnReservar.setOnClickListener {
            val dniUsuario = dialogView.findViewById<EditText>(R.id.InpuDNI).text.toString()
            val nombreUsuario = dialogView.findViewById<EditText>(R.id.InputNombre).text.toString()
            val apellidosUsuario = dialogView.findViewById<EditText>(R.id.InputApellidos).text.toString()
            val nombreMascota = dialogView.findViewById<EditText>(R.id.InputMascota).text.toString()
            val razaMascota = dialogView.findViewById<EditText>(R.id.InputRaza).text.toString()
            val telefonoUsuario = dialogView.findViewById<EditText>(R.id.InputTlfn).text.toString()

            val CamposVacios = mutableListOf<String>()

            if (dniUsuario.isBlank()) CamposVacios.add("DNI")
            if (nombreUsuario.isBlank()) CamposVacios.add("Nombre")
            if (apellidosUsuario.isBlank()) CamposVacios.add("Apellidos")
            if (nombreMascota.isBlank()) CamposVacios.add("Nombre de Mascota")
            if (razaMascota.isBlank()) CamposVacios.add("Raza")
            if (telefonoUsuario.isBlank()) CamposVacios.add("Teléfono")

            if (CamposVacios.isNotEmpty()) {
                mostrarVentanaError(
                    "ERROR",
                    "Introduce los datos. Faltan los siguientes campos:\n${CamposVacios.joinToString(", ")}"
                )
                return@setOnClickListener
            }
            val fechaHora = "$fechaSeleccionada $horaSeleccionada"


            when {
                !dniUsuario.matches("^[0-9A-Za-z]{8,10}$".toRegex()) -> {
                    mostrarVentanaError("ERROR en 'DNI'", "Debe introducir un DNI válido.")
                }
                !nombreUsuario.matches("^[a-zA-Z\\s]+$".toRegex()) -> {
                    mostrarVentanaError("ERROR en 'Nombre'", "Debe introducir solo caracteres de texto.")
                }
                !apellidosUsuario.matches("^[a-zA-Z\\s]+$".toRegex()) -> {
                    mostrarVentanaError("ERROR en 'Apellidos'", "Debe introducir solo caracteres de texto.")
                }
                !razaMascota.matches("^[a-zA-Z\\s]+$".toRegex()) -> {
                    mostrarVentanaError("ERROR en 'Raza'", "Debe introducir solo caracteres de texto.")
                }
                !telefonoUsuario.matches("^\\d{9}$".toRegex()) -> {
                    mostrarVentanaError("ERROR en 'Teléfono'", "Debe introducir nueve dígitos.")
                }
                else -> {
                    dialog.dismiss()
                    procesarReserva(dniUsuario, nombreUsuario, apellidosUsuario, nombreMascota, razaMascota, telefonoUsuario)
                }
            }
        }

        dialog.show()
    }

    private fun procesarReserva(
        dniUsuario: String, nombreUsuario: String, apellidosUsuario: String,
        nombreMascota: String, razaMascota: String, telefonoUsuario: String,
    ) {
        val idPet = "${dniUsuario}_$nombreMascota".replace(" ", "")
        val idCita = "${fechaSeleccionada}_${horaSeleccionada}_$dniUsuario"

        val nuevaMascota = Mascota(
            nombreMascota = nombreMascota,
            idMascota = idPet,
            raza = razaMascota,
            dueno = dniUsuario
        )
        val nuevoUsuario = Usuario(
            nombre = nombreUsuario,
            apellidos = apellidosUsuario,
            telefono = telefonoUsuario,
            dni = dniUsuario
        )

        // Verificar si el usuario ya existe en la base de datos
        FirestoreDBConnector.crearUsuarioSiNoExiste(nuevoUsuario)

        // Verificar si la mascota ya existe en la base de datos
        FirestoreDBConnector.crearMascotaSiNoExiste(nuevaMascota)

        // Crear una nueva cita
        val nuevaCita = Cita(
            idUsuario = dniUsuario,
            idServicio = servicioSeleccionado,
            idCita = idCita,
            nombreCompletoUsuario = "$nombreUsuario $apellidosUsuario",
            mascota = "$nombreMascota ($razaMascota)",
            dia = fechaSeleccionada,
            hora = horaSeleccionada,
            duracion = duracionSeleccionada,
        )

        FirestoreDBConnector.crearCita(nuevaCita,
            onSuccess = {
                Log.d(TAG, "Cita creada: $idCita")
                // Mostrar confirmación al usuario accediendo a los datos de la cita
                mostrarConfirmacion(idCita)
            },
            onFailure = { error ->
                Log.d(TAG, "Error creando cita: $error")
                mostrarVentanaError("Error", "No se pudo crear la cita. Intente nuevamente.")
            })
    }



    //muestra la pantalla resumen cuando se confirma la cita reservada
    private fun mostrarConfirmacion(
        idCita: String // Se usa el ID de la cita como referencia en Firestore
    ) {
        Log.d("LOG", "Iniciando confirmación de reserva para la cita con ID: $idCita")

        FirestoreDBConnector.leerCitaPorID(idCita) { result, error ->
            if (error != null) {
                mostrarVentanaError("Error", "Error al consultar la cita creada")
            } else if (result != null) {
                mostrarConfirmacionCita(result)
            } else {
                mostrarVentanaError("Error", "No se encontró la cita con ID $idCita")
            }
        }
    }

    private fun mostrarConfirmacionCita(cita: Cita) {
        val dialogView = layoutInflater.inflate(R.layout.pantalla_confirmacion_reserva, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val textResumenCita = dialogView.findViewById<TextView>(R.id.TextResumenCita)
        val btnAceptar = dialogView.findViewById<Button>(R.id.ButtonAceptar)

        if (textResumenCita == null || btnAceptar == null) {
            mostrarVentanaError(
                "Error",
                "No se pudieron cargar los elementos necesarios en el diseño de confirmación."
            )
            return
        }

        val resumen = """
                Servicio: "${cita.idServicio} - $precioSeleccionado €"
                DNI: ${cita.idUsuario}
                Nombre: ${cita.nombreCompletoUsuario}
                Mascota: ${cita.mascota} 
                Fecha y Hora: ${cita.dia} - ${cita.hora}
           """ .trimIndent()

        textResumenCita.text = resumen

        // Configurar acción para el botón Aceptar
        btnAceptar.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, Pantalla_Menu::class.java)
            startActivity(intent)
            finish()
        }

        dialog.show()
    }


    private fun mostrarVentanaError(titulo: String, mensaje: String) {
        val errorDialog = AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
            .setCancelable(true)
            .create()
        errorDialog.show()
    }
}
