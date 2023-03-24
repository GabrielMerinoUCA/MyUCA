package ni.edu.uca.myuca

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.coroutines.*
import ni.edu.uca.myuca.databinding.FragmentNuevoEstudianteBinding
import okhttp3.*
import java.io.IOException


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NuevoEstudiante.newInstance] factory method to
 * create an instance of this fragment.
 */
class NuevoEstudiante : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var fbinding: FragmentNuevoEstudianteBinding

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
        fbinding = FragmentNuevoEstudianteBinding.inflate(layoutInflater)
        initialize()
        return fbinding.root
    }

    private fun initialize() {
        fbinding.btnAgregarNew.setOnClickListener {
            initPOST()
            Navigation.findNavController(fbinding.root).navigate(R.id.acNuevoEstudiante_Home2)
        }
    }

    /**
     * La razon por la que esta funcion esta toda rara es porque soy imbecil y se
     * me olvido poner xxx.text.toString.
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun initPOST() {

        var nombres = fbinding.etNombresNew.text.toString()
        var apellidos = fbinding.etApellidosNew.text.toString()
        var carrera = fbinding.etCarreraNew.text.toString()
        var year = fbinding.etYearNew.text.toString()

        val client = OkHttpClient().newBuilder().build()
        var body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
            "nombres=${nombres}" +
                    "&apellidos=${apellidos}" +
                    "&carrera=${carrera}" +
                    "&years=${year}")

        /* CAMBIAR URL */
        var request = Request.Builder()
            .url("http://192.168.1.14/MyUCA/insertarEstudiante.php")
            .method("POST", body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    try {
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