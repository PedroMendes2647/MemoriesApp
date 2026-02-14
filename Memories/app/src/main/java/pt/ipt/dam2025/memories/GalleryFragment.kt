package pt.ipt.dam2025.memories


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter
    private val listaFotos = mutableListOf<Foto>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        recyclerView = view.findViewById(R.id.recyclerFotos)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        adapter = GalleryAdapter(listaFotos) { foto ->
            abrirDetalhe(foto)
        }

        recyclerView.adapter = adapter

        carregarFotos()

        return view
    }
    //PROBLEMA AQUI
    private fun carregarFotos() {
        val userId = activity?.intent?.getIntExtra("USER_ID", 1) ?: 1

        RetrofitClient.instance.listarFotos(userId)
            .enqueue(object : Callback<List<Foto>> {
                override fun onResponse(
                    call: Call<List<Foto>>,
                    response: Response<List<Foto>>
                ) {
                    if (response.isSuccessful) {
                        val fotos = response.body() ?: emptyList()
                        listaFotos.clear()
                        listaFotos.addAll(fotos)
                        adapter.notifyDataSetChanged()

                        if (fotos.isEmpty()) {
                            Toast.makeText(context, "Nenhuma foto encontrada", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<List<Foto>>?, t: Throwable?) { // <--- nullable
                    Toast.makeText(context, "Erro ao carregar fotos", Toast.LENGTH_SHORT).show()
                }
            })
    }
/**
    private fun carregarFotos() {
        val userId = activity?.intent?.getIntExtra("USER_ID", 1) ?: 1

        RetrofitClient.instance.listarFotos(userId)
            .enqueue(object : Callback<List<Foto>> {
                override fun onResponse(
                    call: Call<List<Foto>>,
                    response: Response<List<Foto>>
                ) {
                    if (response.isSuccessful) {
                        listaFotos.clear()
                        listaFotos.addAll(response.body()!!)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<Foto>>, t: Throwable) {
                    Toast.makeText(context, "Erro ao carregar fotos", Toast.LENGTH_SHORT).show()
                }
            })
    }
*/
    private fun abrirDetalhe(foto: Foto) {
        val dialog = PhotoDetailDialogFragment(foto) {
            carregarFotos() // refresh after delete
        }
        dialog.show(parentFragmentManager, "detalhe")
    }
}