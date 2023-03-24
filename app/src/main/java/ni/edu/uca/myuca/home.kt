package ni.edu.uca.myuca

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ni.edu.uca.myuca.Modelos.Estudiante
import ni.edu.uca.myuca.adapters.EstudianteAdapter
import ni.edu.uca.myuca.databinding.FragmentHomeBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.math.log

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [home.newInstance] factory method to
 * create an instance of this fragment.
 */
class home : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var fbinding: FragmentHomeBinding
    private lateinit var estudiantes: ArrayList<Estudiante>

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
        fbinding = FragmentHomeBinding.inflate(layoutInflater)
        initialize()
        return fbinding.root
    }

    private fun initialize() {
        loadEstudiantes()
        fbinding.btnNuevo.setOnClickListener {
            Navigation.findNavController(fbinding.root).navigate(R.id.acHome2_nuevoEstudiante)
        }
    }

    private fun loadEstudiantes() {
        estudiantes = ArrayList()
        /*.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Connection", "close").build();
                return chain.proceed(request);
            }
        })*/
        val client = OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS) // Timeout para conectarse al server
            .readTimeout(10, TimeUnit.SECONDS) // Timeout para recibir respuesta del server
            .build()

        /* CAMBIAR URL */
        val apiUrl = "http://192.168.1.14/MyUCA/mostrarEstudiantes.php"
        val request: Request = Request.Builder()
            .url(apiUrl)
            .header("Connection", "close")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    try {
                        /* { "data": [{ "id": "1", "nombres": "Pepe", "apellidos": "Ayote", "carrera": "ISI", "years": "1" },]} */
                        val body = response.body()!!.string()
                        val jsonObject = JSONObject(body)
                        Log.wtf("RESPUESTA: ", body)
                        val dataArray = jsonObject.getJSONArray("data")
                        val leght = dataArray.length()
                        var i = 0
                        do {
                            if (leght != 0) {
                                val estudianteJSON = dataArray.getJSONObject(i)
                                val id: Int = estudianteJSON.getString("id").toInt()
                                val nombres: String = estudianteJSON.getString("nombres")
                                val apellidos: String = estudianteJSON.getString("apellidos")
                                val carrera: String = estudianteJSON.getString("carrera")
                                val years: Int = estudianteJSON.getString("years").toInt()
                                estudiantes.add(
                                    Estudiante(
                                        id,
                                        nombres,
                                        apellidos,
                                        carrera,
                                        years
                                    )
                                )
                                i++
                            }
                        } while (i < leght - 1)

                        GlobalScope.launch(Dispatchers.Main) {
                            initRV()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        /* Esto evita que los datos no se carguen pero no es una muy buena solucion
                        * debido a que puede ocacionar bucles infinitos; y pues, no resuelve el
                        * problema de la exepcion que tira cuando no se reciben datos.*/
                        loadEstudiantes()
                    }
                }
            }
        })
    }


    private fun initRV() {
        val recyclerView = fbinding.rvEstudiantes
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = EstudianteAdapter(estudiantes, fbinding.root)
    }

}