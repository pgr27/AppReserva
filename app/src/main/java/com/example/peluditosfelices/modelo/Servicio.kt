package com.example.peluditosfelices.modelo

import com.google.firebase.firestore.PropertyName

data class Servicio(
    @PropertyName("Id_NombreServicio") val id_nombreServicio: String = "",
    @PropertyName("Descripcion") val descripcion: String = "",
    @PropertyName("Duracion") val duracion: Int = 0,
    @PropertyName("Precio") val precio: Double = 0.0,
    @PropertyName("TipoServicio") val tipoServicio: String = ""
)
