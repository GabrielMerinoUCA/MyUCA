package ni.edu.uca.myuca.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import ni.edu.uca.myuca.Modelos.Estudiante
import ni.edu.uca.myuca.R
import ni.edu.uca.myuca.homeDirections

class EstudianteAdapter(var estudiantes: ArrayList<Estudiante>, val currentView: View) :
    RecyclerView.Adapter<EstudianteAdapter.EstudianteViewHolder>() {
    override fun getItemCount(): Int = estudiantes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudianteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EstudianteViewHolder(layoutInflater.inflate(R.layout.rv_estudiante, parent, false))
    }

    override fun onBindViewHolder(holder: EstudianteViewHolder, position: Int) {
        val estudiante: Estudiante = estudiantes[position]
        holder.load(estudiante, currentView)
    }

    inner class EstudianteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var tvNombre = view.findViewById<TextView>(R.id.tvNombres)
        private var tvApellidos = view.findViewById<TextView>(R.id.tvApellidos)
        private var tvCarrera = view.findViewById<TextView>(R.id.tvCarrera)
        private var tvYear = view.findViewById<TextView>(R.id.tvYear)
        private var rvEstudiante = view.findViewById<ConstraintLayout>(R.id.rvEstudiantesMostrar)
        private var id = 0
        @SuppressLint("SetTextI18n")
        fun load(estudiante: Estudiante, origin: View) {
            id = estudiante.id
            tvNombre.text = tvNombre.text.toString() + estudiante.nombres
            tvApellidos.text = tvApellidos.text.toString() + estudiante.apellidos
            tvCarrera.text = tvCarrera.text.toString() + estudiante.carrera
            tvYear.text = tvYear.text.toString() + estudiante.year
            rvEstudiante.setOnClickListener{
                val action = homeDirections.acHome2ModificarEstudiante(estudiante)
                Navigation.findNavController(origin).navigate(action)
            }

        }
    }
}