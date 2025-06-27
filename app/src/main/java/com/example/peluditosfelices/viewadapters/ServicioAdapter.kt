package com.example.peluditosfelices.viewadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.peluditosfelices.R
import com.example.peluditosfelices.modelo.Servicio

class ServicioAdapter(
    private val servicios: List<Servicio>,
    private val onClickServicio: (Servicio) -> Unit
) : RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder>() {

    inner class ServicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre = itemView.findViewById<TextView>(R.id.tvNombre)
        val tvDescripcion = itemView.findViewById<TextView>(R.id.tvDescripcion)
        val tvDuracion = itemView.findViewById<TextView>(R.id.tvDuracion)
        val tvPrecio = itemView.findViewById<TextView>(R.id.tvPrecio)

        fun bind(servicio: Servicio) {
            tvNombre.text = servicio.id_nombreServicio
            tvDescripcion.text = servicio.descripcion
            tvPrecio.text = "Precio: ${servicio.precio}€"
            tvDuracion.text = "Duración: ${servicio.duracion} min"

            // Hacer que el área completa sea clicable
            itemView.setOnClickListener {
                onClickServicio(servicio)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_servicio, parent, false)
        return ServicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServicioViewHolder, position: Int) {
        val servicio = servicios[position]
        holder.bind(servicio)
    }

    override fun getItemCount(): Int = servicios.size
}
