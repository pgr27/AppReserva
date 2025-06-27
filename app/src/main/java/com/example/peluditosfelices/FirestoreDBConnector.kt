package com.example.peluditosfelices

import android.util.Log
import com.example.peluditosfelices.modelo.Cita
import com.example.peluditosfelices.modelo.Contacto
import com.example.peluditosfelices.modelo.Mascota
import com.example.peluditosfelices.modelo.Servicio
import com.example.peluditosfelices.modelo.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//En esta clase se añadirá todos los métodos relacionados con la BBDD en Firestore.
class FirestoreDBConnector {
    companion object {
        // Define un bloque estático (con companion) que permite acceder a funciones y propiedades
        // sin necesidad de instanciar la clase.
        private val db: FirebaseFirestore by lazy {
            FirebaseFirestore.getInstance()
        }
        private const val TAG = "FireStoreDBConnector";

        fun obtenerInfoNegocio(onSuccess: (Contacto?) -> Unit, onFailure: (String) -> Unit) {
            db.collection("Negocio")
                .document("PyF")
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val contacto = document.toObject(Contacto::class.java)
                        onSuccess(contacto)
                    } else {
                        // Documento no encontrado
                        Log.w(TAG, "No se encontró ninguna información para el negocio.")
                        onFailure("No se encontró ninguna información para el negocio.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "No se encontró ninguna información para el negocio.", exception)
                    onFailure("No se encontró ninguna información para el negocio.")
                }
        }

        fun nuevoUsuario(usuario: Usuario) {
            // Usar el DNI como identificador único del documento
            db.collection("Usuarios")
                .document(usuario.dni)
                .set(usuario)
                .addOnSuccessListener {
                    Log.d(TAG, "Usuario agregado correctamente: $usuario")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error al crear nuevo usuario $usuario.", e)
                }
        }

        fun leerUsuario(dni: String, onSuccess: (Map<String, Any>) -> Unit, onFailure: (String) -> Unit) {
            // Leer documentos de la colección "Usuarios"
            db.collection("Usuarios")
                .document(dni) // Buscar directamente por el nombre del documento
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val datosUsuario = document.data
                        datosUsuario?.let { onSuccess(it) }
                    } else {
                        // Documento no encontrado
                        Log.w(TAG, "No se encontró ningún usuario con el DNI proporcionado.")
                        onFailure("No se encontró ningún usuario con el DNI proporcionado.")
                    }
                }
                .addOnFailureListener { exception ->
                    // Error al acceder a Firestore
                    Log.e(TAG, "Error al obtener el usuario.", exception)
                    onFailure("No se encontró ningún usuario con el DNI proporcionado.")
                }
        }

        fun crearUsuarioSiNoExiste(usuario: Usuario) {
            leerUsuario(usuario.dni,
                onSuccess = { datosUsuario ->
                    // Usuario encontrado.
                    Log.d(TAG, "Usuario encontrado: $datosUsuario")
                },
                onFailure = { mensajeError ->
                    // No existe el usuario, por tanto, lo creamos.
                    Log.w(TAG, mensajeError)
                    nuevoUsuario(usuario)
                }
            )
        }

        fun nuevaMascota(mascota: Mascota) {
            // Usar el DNI como identificador único del documento
            db.collection("Mascota")
                .document(mascota.idMascota)
                .set(mascota)
                .addOnSuccessListener {
                    Log.d(TAG, "Mascota agregada correctamente: $mascota")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error al crear nueva mascota $mascota.", e)
                }
        }

        fun leerMascota(idMascota: String, onSuccess: (Map<String, Any>) -> Unit, onFailure: (String) -> Unit) {
            // Leer documentos de la colección "Mascotas"
            db.collection("Mascota")
                .document(idMascota) // Buscar directamente por el nombre del documento
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val datosMascota = document.data
                        datosMascota?.let { onSuccess(it) } // Llamar a la función de éxito con los datos
                    } else {
                        // Documento no encontrado
                        Log.w(TAG, "No se encontró ninguna mascota con el ID proporcionado: $idMascota.")
                        onFailure("No se encontró ninguna mascota con el ID proporcionado: $idMascota.")
                    }
                }
                .addOnFailureListener { exception ->
                    // Error al acceder a Firestore
                    Log.e(TAG, "Error al obtener mascota $idMascota.", exception)
                    onFailure("No se encontró ninguna mascota con el ID proporcionado: $idMascota.")
                }
        }

        fun crearMascotaSiNoExiste(mascota: Mascota) {
            leerMascota(mascota.idMascota,
                onSuccess = { datosMascota ->
                    // Mascota encontrada. No hay que hacer nada
                    Log.d(TAG, "Mascota encontrada: $datosMascota")
                },
                onFailure = { mensajeError ->
                    // No existe la mascota, por tanto, la creamos.
                    Log.w(TAG, mensajeError)
                    nuevaMascota(mascota)
                }
            )
        }

        fun obtenerServicios(callback: (List<Servicio>) -> Unit) {
            db.collection("Servicio")
                .get()
                .addOnSuccessListener { result ->
                    val servicios = result.map { document ->
                        Log.d(TAG, "Servicio: " + document)
                        document.toObject(Servicio::class.java)
                    }
                    Log.d(TAG, "Servicios: " + servicios)
                    callback(servicios)
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreError", "Error al obtener servicios", exception)
                    callback(emptyList())
                }
        }

        fun crearCita(cita: Cita, onSuccess: (String?) -> Unit, onFailure: (String) -> Unit) {
            // Usar el Id_Cita como identificador único del documento
            db.collection("Citas")
                .document(cita.idCita.orEmpty())
                .set(cita)
                .addOnSuccessListener {
                    Log.d(TAG, "Nueva cita añadida correctamente: $cita")
                    onSuccess(cita.idCita)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error al añadir nueva cita.", e)
                    onFailure(e.toString())
                }
        }

        fun leerCitasParaUsuario(dni: String, callback: (result: QuerySnapshot?, error: Exception?) -> Unit) {
            db.collection("Citas")
                .whereEqualTo("idUsuario", dni)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(task.result, null)
                    } else {
                        callback(null, task.exception)
                    }
                }
        }

        fun leerCitaPorID(idCita: String, callback: (result: Cita?, error: Exception?) -> Unit) {
            db.collection("Citas")
                .whereEqualTo("idCita", idCita)
                .limit(1)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result?.documents?.firstOrNull() // Obtener el primer documento si existe
                        if (document != null) {
                            // Convertir el documento a un objeto Cita
                            val cita = document.toObject(Cita::class.java)
                            callback(cita, null)
                        } else {
                            // No se encontró ningún documento
                            callback(null, null)
                        }
                    } else {
                        callback(null, task.exception)
                    }
                }
        }

        fun obtenerHorasPorDiaYDuracion(dia: String, duracion: Int, callback: (result: List<String>?, error: Exception?) -> Unit) {
            db.collection("Citas")
                .whereEqualTo("dia", dia) // Consultar por el campo "Fecha"
                .get()
                .addOnSuccessListener { result ->
                    val citas = result.mapNotNull { it.toObject(Cita::class.java) }
                    //callback(citas, null) // Devolver la lista de objetos Cita
                    obtenerInfoNegocio(
                        onSuccess = { infoContacto ->
                            val esDiaLV = compruebaDiaLV(dia)
                            val manana : Boolean
                            val tarde : Boolean
                            if(esDiaLV) {
                                manana = !infoContacto?.horarioLV_iM.isNullOrEmpty() && !infoContacto?.horarioLV_fM.isNullOrEmpty()
                                tarde = !infoContacto?.horarioLV_iT.isNullOrEmpty() && !infoContacto?.horarioLV_fT.isNullOrEmpty()
                            } else {
                                manana = !infoContacto?.horarioS_iM.isNullOrEmpty() && !infoContacto?.horarioS_fM.isNullOrEmpty()
                                tarde = !infoContacto?.horarioS_iT.isNullOrEmpty() && !infoContacto?.horarioS_fT.isNullOrEmpty()
                            }

                            val horas: MutableList<String> = mutableListOf()

                            if(esDiaLV) {
                                if (manana) {
                                    horas.addAll(
                                        generarHorasDisponibles(
                                            infoContacto?.horarioLV_iM.orEmpty(),
                                            infoContacto?.horarioLV_fM.orEmpty(),
                                            citas,
                                            duracion
                                        )
                                    )
                                }
                                if (tarde) {
                                    horas.addAll(
                                        generarHorasDisponibles(
                                            infoContacto?.horarioLV_iT.orEmpty(),
                                            infoContacto?.horarioLV_fT.orEmpty(),
                                            citas,
                                            duracion
                                        )
                                    )
                                }
                            } else {
                                if (manana) {
                                    horas.addAll(
                                        generarHorasDisponibles(
                                            infoContacto?.horarioS_iM.orEmpty(),
                                            infoContacto?.horarioS_fM.orEmpty(),
                                            citas,
                                            duracion
                                        )
                                    )
                                }
                                if (tarde) {
                                    horas.addAll(
                                        generarHorasDisponibles(
                                            infoContacto?.horarioS_iT.orEmpty(),
                                            infoContacto?.horarioS_fT.orEmpty(),
                                            citas,
                                            duracion
                                        )
                                    )
                                }
                            }

                            // Ordenamos las horas
                            horas.sort()
                            callback(horas, null)
                        },
                        onFailure = { error ->
                            Log.d(TAG, error)
                            callback(null, Exception(error))
                        }
                    )
                }
                .addOnFailureListener { exception ->
                    callback(null, exception) // Manejar errores
                }
        }

        fun generarHorasDisponibles(
            inicioHorario: String,
            finHorario: String,
            citas: List<Cita>,
            duracion: Int
        ): List<String> {
            val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
            val calendarioInicio = Calendar.getInstance()
            val calendarioFin = Calendar.getInstance()

            // Parsear las horas de inicio y fin
            calendarioInicio.time = formatoHora.parse(inicioHorario)!!
            calendarioFin.time = formatoHora.parse(finHorario)!!

            val horasDisponibles = mutableListOf<String>()

            // Generar bloques de tiempo según la duración especificada
            while (calendarioInicio.before(calendarioFin)) {
                val horaActual = formatoHora.format(calendarioInicio.time)

                // Calcular el fin del bloque actual según la duración
                val calendarioFinBloque = Calendar.getInstance()
                calendarioFinBloque.time = calendarioInicio.time
                calendarioFinBloque.add(Calendar.MINUTE, duracion)

                // Verificar si el bloque actual está dentro del horario permitido
                if (calendarioFinBloque.after(calendarioFin)) {
                    break
                }

                // Verificar si el bloque actual está ocupado por alguna cita
                val estaOcupado = citas.any { cita ->
                    val horaCitaInicio = formatoHora.parse(cita.hora!!)
                    val horaCitaFin = Calendar.getInstance().apply {
                        time = horaCitaInicio
                        add(Calendar.MINUTE, cita.duracion!!.toInt())
                    }

                    // Comprobar si el bloque actual se superpone con la cita
                    calendarioInicio.time >= horaCitaInicio && calendarioInicio.time < horaCitaFin.time ||
                            calendarioFinBloque.time > horaCitaInicio && calendarioFinBloque.time < horaCitaFin.time ||
                            (horaCitaInicio >= calendarioInicio.time && horaCitaInicio < calendarioFinBloque.time)
                }

                // Si el bloque no está ocupado, agregarlo al resultado
                if (!estaOcupado) {
                    horasDisponibles.add(horaActual)
                }

                // Avanzar 30 minutos
                calendarioInicio.add(Calendar.MINUTE, 30)
            }

            return horasDisponibles
        }
        fun leerDatos(
            collectionPath: String,
            onSuccess: (List<Map<String, Any>>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            db.collection(collectionPath)
                .get()
                .addOnSuccessListener { result ->
                    val datos = result.map { it.data }
                    onSuccess(datos)
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error al leer datos de la colección $collectionPath", exception)
                    onFailure(exception)
                }
        }

        fun compruebaDiaLV(dia: String) : Boolean {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fecha = formatter.parse(dia)

            // Crear instancia de Calendar y establecer la fecha
            val calendar = Calendar.getInstance()
            calendar.time = fecha

            // Obtener el día de la semana
            return when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SATURDAY, Calendar.SUNDAY -> false
                else -> true
            }
        }
    }

}

