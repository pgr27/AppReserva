package com.example.peluditosfelices.modelo

data class Cita(
    val idServicio: String? = null,
    val idUsuario: String? = null,
    val idCita: String? = null,
    val nombreCompletoUsuario: String ?=null,
    val mascota: String? = null,
    val dia: String? = null,
    val hora: String? = null,
    val duracion: Int? = null,
)
