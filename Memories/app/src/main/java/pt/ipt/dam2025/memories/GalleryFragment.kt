package pt.ipt.dam2025.memories


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GalleryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val userId = activity?.intent?.getIntExtra("USER_ID", -1)

        if (userId != null && userId != -1) {
            RetrofitClient.instance.getFotos(userId)
                .enqueue(object : Callback<List<Foto>> {
                    override fun onResponse(
                        call: Call<List<Foto>>,
                        response: Response<List<Foto>>
                    ) {
                        if (response.isSuccessful) {
                            val fotos = response.body()
                            println(fotos) // For now just log
                        }
                    }

                    override fun onFailure(call: Call<List<Foto>>, t: Throwable) {
                        println("Error loading photos")
                    }
                })
        }

        return view
    }
}