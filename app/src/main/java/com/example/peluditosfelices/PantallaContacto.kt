package com.example.peluditosfelices

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.peluditosfelices.modelo.Contacto

class PantallaContacto : AppCompatActivity() {

    private var contactoInfo: Contacto? = null
    private val TAG: String = "PantallaContacto"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_contacto)

        // Crear un OnBackPressedCallback y registrarlo para controlar la acción del botón de volver
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@PantallaContacto, Pantalla_Menu::class.java)
                startActivity(intent)
                finish() // Cierra la pantalla actual para evitar regresar aquí
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)

        FirestoreDBConnector.obtenerInfoNegocio(
            onSuccess = { contacto ->
                Log.d(TAG, "" + contacto)
                contactoInfo = contacto
                onSuccessRetrieval(contacto)
            },
            onFailure = { error ->
                Log.d(TAG, error)
                contactoInfo = Contacto()
            }
        )


    }

    private fun onSuccessRetrieval(contacto: Contacto?) {
        val tvDireccionContacto = findViewById<TextView>(R.id.tvDireccionContacto)
        val tvTelefonoContacto = findViewById<TextView>(R.id.tvTelefonoContacto)
        val tvHorarioLV = findViewById<TextView>(R.id.tvHorarioLV)
        val tvHorarioS = findViewById<TextView>(R.id.tvHorarioS)

        // Configura los datos iniciales en la vista
        tvDireccionContacto.text = contactoInfo?.direccion?.replace("\\n", "\n")
        tvTelefonoContacto.text = "${contactoInfo?.telefono} (${contactoInfo?.propietario})"
        tvHorarioLV.text = formateaHorarioParaVista("LV")
        tvHorarioS.text = formateaHorarioParaVista("S")

        // Botón "Flecha" para volver al menú
        val backButton = findViewById<ImageView>(R.id.ButtonVolver)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Botón de ubicación
        val btnUbicacion = findViewById<ImageView>(R.id.ButtonUbicacion)
        btnUbicacion.setOnClickListener {
            // Crear un Intent para abrir Google Maps con la dirección especificada
            val gmmIntentUri = Uri.parse("geo:0,0?q=${contactoInfo?.direccion?.replace("\\n", "")}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            val resolvedActivities = this.packageManager.queryIntentActivities(mapIntent, PackageManager.MATCH_DEFAULT_ONLY)

            if (resolvedActivities.isNotEmpty()) {
                startActivity(mapIntent)
            } else {
                // Mostrar ventana de error si Google Maps no está instalado
                mostrarVentanaError(
                    "Google Maps no disponible",
                    "Google Maps no está instalado en este dispositivo. Por favor, instale la aplicación para usar esta función."
                )
            }
        }

        // Botón de teléfono
        val btnTelefono = findViewById<ImageView>(R.id.ButtonTelefono)
        btnTelefono.setOnClickListener {
            // Crear un Intent para abrir la aplicación de llamadas con el número especificado
            val phoneNumber = "tel:+34 ${contactoInfo?.telefono}"
            val callIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse(phoneNumber)
            }

            // Verificar que el Intent puede ser manejado
            if (callIntent.resolveActivity(packageManager) != null) {
                startActivity(callIntent)
            } else {
                // Mostrar ventana de error si no se puede abrir el entorno de llamadas
                mostrarVentanaError(
                    "Teléfono no disponible",
                    "No se puede abrir la aplicación de llamadas en este dispositivo. Por favor, compruebe la configuración."
                )
            }
        }
    }

    // Método para mostrar una ventana de error
    private fun mostrarVentanaError(titulo: String, mensaje: String) {
        val errorDialog = AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .create()
        errorDialog.show()
    }

    private fun formateaHorarioParaVista(opcion: String) : String {
        var horario = ""
        when(opcion) {
            "LV" -> {
                if(!contactoInfo?.horarioLV_iM.isNullOrEmpty() && !contactoInfo?.horarioLV_fM.isNullOrEmpty()) {
                    horario += "${contactoInfo?.horarioLV_iM} - ${contactoInfo?.horarioLV_fM}"
                    if(!contactoInfo?.horarioLV_iT.isNullOrEmpty() && !contactoInfo?.horarioLV_fT.isNullOrEmpty()) {
                        horario += "\n"
                    }
                }
                if(!contactoInfo?.horarioLV_iT.isNullOrEmpty() && !contactoInfo?.horarioLV_fT.isNullOrEmpty()) {
                    horario += "${contactoInfo?.horarioLV_iT} - ${contactoInfo?.horarioLV_fT}"
                }
            }
            "S" -> {
                if(!contactoInfo?.horarioS_iM.isNullOrEmpty() && !contactoInfo?.horarioS_fM.isNullOrEmpty()) {
                    horario += "${contactoInfo?.horarioS_iM} - ${contactoInfo?.horarioS_fM}"
                    if(!contactoInfo?.horarioS_iT.isNullOrEmpty() && !contactoInfo?.horarioS_fT.isNullOrEmpty()) {
                        horario += "\n"
                    }
                }
                if(!contactoInfo?.horarioS_iT.isNullOrEmpty() && !contactoInfo?.horarioS_fT.isNullOrEmpty()) {
                    horario += "${contactoInfo?.horarioS_iT} - ${contactoInfo?.horarioS_fT}"
                }
            }
            else -> {
                horario = "No disponible"
            }
        }
        return horario
    }
}
