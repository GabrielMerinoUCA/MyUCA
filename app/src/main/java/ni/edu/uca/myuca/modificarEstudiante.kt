package ni.edu.uca.myuca

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ni.edu.uca.myuca.Modelos.Estudiante
import ni.edu.uca.myuca.databinding.FragmentModificarEstudianteBinding
import okhttp3.*
import java.io.IOException

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [modificarEstudiante.newInstance] factory method to
 * create an instance of this fragment.
 */
class modificarEstudiante : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val args: modificarEstudianteArgs by navArgs()
    private lateinit var fbinding: FragmentModificarEstudianteBinding
    private lateinit var estudiante: Estudiante

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fbinding = FragmentModificarEstudianteBinding.inflate(layoutInflater)
        initialize()
        return fbinding.root
    }

    private fun initialize() {
        estudiante = args.estudiante
        fbinding.etNombresUpdate.setText(estudiante.nombres)
        fbinding.etApellidosUpdate.setText(estudiante.apellidos)
        fbinding.etCarreraUpdate.setText(estudiante.carrera)
        fbinding.etYearUpdate.setText(estudiante.year.toString())

        fbinding.btnUpdate.setOnClickListener {
            updateEstudiante()
        }
        fbinding.btnEliminar.setOnClickListener {
            eliminarEstudiante()
        }
    }

    private fun eliminarEstudiante() {
        var client = OkHttpClient().newBuilder().build()
        var body = RequestBody.create(
            MediaType.parse("application/x-www-form-urlencoded"),
            "[\"${estudiante.id.toString()}\"]")

        /* CAMBIAR URL */
        var request = Request.Builder()
            .url("http://192.168.1.14/MyUCA/deleteEstudiante.php")
            .method("DELETE", body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    try {
                        Log.wtf("AAAA", response.body()!!.string())
                        GlobalScope.launch(Dispatchers.Main) {
                            Toast.makeText(context, "Eliminado exitosamente", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        Navigation.findNavController(fbinding.root).navigate(R.id.acModificarEstudiante_home2)
    }

    private fun updateEstudiante() {
        var client = OkHttpClient().newBuilder().build()
        var POST = "[\"${estudiante.id.toString()}\" ," +
                "\"${fbinding.etNombresUpdate.text.toString()}\" ," +
                "\"${fbinding.etApellidosUpdate.text.toString()}\" ," +
                "\"${fbinding.etCarreraUpdate.text.toString()}\" ," +
                "\"${fbinding.etYearUpdate.text.toString()}\"]"
        Log.wtf("POST:", POST)
        var body = RequestBody.create(
            MediaType.parse("application/x-www-form-urlencoded"),
            "[\"${estudiante.id.toString()}\" ," +
                    "\"${fbinding.etNombresUpdate.text.toString()}\" ," +
                    "\"${fbinding.etApellidosUpdate.text.toString()}\" ," +
                    "\"${fbinding.etCarreraUpdate.text.toString()}\" ," +
                    "\"${fbinding.etYearUpdate.text.toString()}\"]")

        /* CAMBIAR URL */
        var request = Request.Builder()
            .url("http://192.168.1.14/MyUCA/updateEstudiante.php")
            .method("PUT", body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    try {
                        Log.wtf("AAAA", response.body()!!.string())
                        GlobalScope.launch(Dispatchers.Main) {
                            Toast.makeText(context, "Guardado exitosamente", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Error de guardado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}