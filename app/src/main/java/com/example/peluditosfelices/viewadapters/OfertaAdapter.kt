package com.example.peluditosfelices.viewadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.peluditosfelices.R
import com.example.peluditosfelices.modelo.Oferta

class OfertaAdapter(
    private val ofertas: List<Oferta> // Lista de ofertas que se mostrarán
) : RecyclerView.Adapter<OfertaAdapter.OfertaViewHolder>() {

    // ViewHolder interno que maneja las vistas individuales de cada oferta
    inner class OfertaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        private val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        private val tvDuracion: TextView = itemView.findViewById(R.id.tvDuracion)

        fun bind(oferta: Oferta) {
            // Asignar valores de la oferta a las vistas
            tvTitulo.text = oferta.Titulo.orEmpty()
            tvDescripcion.text = oferta.Descripcion.orEmpty()
            tvDuracion.text = oferta.Duracion.orEmpty()
        }
    }

    // Crear nuevas vistas cuando el RecyclerView las necesite
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfertaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_oferta, parent, false)
        return OfertaViewHolder(view)
    }

    // Vincular datos con las vistas en la posición actual
    override fun onBindViewHolder(holder: OfertaViewHolder, position: Int) {
        holder.bind(ofertas[position])
    }

    // Indicar cuántos elementos hay en la lista
    override fun getItemCount(): Int = ofertas.size
}
