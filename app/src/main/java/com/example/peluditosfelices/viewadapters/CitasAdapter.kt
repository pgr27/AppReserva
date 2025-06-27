package com.example.peluditosfelices.viewadapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.peluditosfelices.R
import com.example.peluditosfelices.modelo.Cita

class CitasAdapter(
    private val citas: List<Cita>, // Lista de citas que se mostrarán
    private val onClickCita: (Cita) -> Unit, // Función lambda para manejar clics en las citas
    private val onEvaluarCita: (Cita, View) -> Unit
) : RecyclerView.Adapter<CitasAdapter.CitasViewHolder>() {

    //Relacion de bbdd con la vista cita
    // ViewHolder interno que maneja las vistas individuales de cada cita
    inner class CitasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvServicio: TextView = itemView.findViewById(R.id.tvServicio)
        private val tvnombreUsuario: TextView = itemView.findViewById(R.id.tvNombreUsuario)
        private val tvNombreMascota: TextView = itemView.findViewById(R.id.tvNombreMascota)
        private val tvfechaHora: TextView = itemView.findViewById(R.id.tvfechaHora)

        fun bind(cita: Cita) {
            // Asignar valores de la cita a las vistas
            tvServicio.text = "Servicio: ${cita.idServicio.orEmpty()}"
            tvnombreUsuario.text = "Nombre: ${cita.nombreCompletoUsuario.orEmpty()}"
            tvNombreMascota.text = "Mascota: ${cita.mascota.orEmpty()}"
            tvfechaHora.text = "Fecha: ${cita.dia.orEmpty()} - ${cita.hora.orEmpty()}"

            // Configurar un clic en el elemento
            itemView.setOnClickListener {
                onClickCita(cita)
            }

            onEvaluarCita(cita, itemView.findViewById(R.id.viewItemCita))
        }
    }

    // Crear nuevas vistas cuando el RecyclerView las necesite
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitasViewHolder(view)
    }

    // Vincular datos con las vistas en la posición actual
    override fun onBindViewHolder(holder: CitasViewHolder, position: Int) {
        Log.d("CitasAdapter", "Cargando datos: $citas")
        holder.bind(citas[position])
    }

    // Indicar cuántos elementos hay en la lista
    override fun getItemCount(): Int = citas.size
}
