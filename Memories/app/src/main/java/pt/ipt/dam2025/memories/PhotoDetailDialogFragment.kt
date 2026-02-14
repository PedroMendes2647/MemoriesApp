package pt.ipt.dam2025.memories

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.net.Uri
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
/**Retrofit: Biblioteca para tornar a API em uma interface de Kotlin/Java. https://square.github.io/retrofit/ */

/** DialogFragment para mostrar os detalhes de uma foto, incluindo a imagem, descrição e coordenadas. Permite também eliminar a foto. */
class PhotoDetailDialogFragment(
    private val foto: Foto,
    private val onDeleted: () -> Unit
) : DialogFragment() {
/** * Cria a view do dialog, carregando a imagem de forma segura e configurando os detalhes da foto. */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.dialog_photo_detail, container, false)

        val img = view.findViewById<ImageView>(R.id.imgDetail)
        val desc = view.findViewById<TextView>(R.id.txtDescricao)
        val coords = view.findViewById<TextView>(R.id.txtCoords)
        val deleteBtn = view.findViewById<Button>(R.id.btnDelete)

        // Null-safe image loading
        foto.imagem?.let { path ->
            try {
                img.setImageURI(Uri.parse(path))
            } catch (_: Exception) {
                // ignore
            }
        } ?: img.setImageResource(R.drawable.placeholder)

        desc.text = foto.descricao
        coords.text = "Lat: ${foto.latitude}  Lon: ${foto.longitude}"

        deleteBtn.setOnClickListener {
            apagarFoto()
        }

        return view
    }
/** * Apaga a foto do servidor usando Retrofit. Se a foto ainda não tiver um ID, apenas fecha o dialog e notifica para atualizar. */
    private fun apagarFoto() {
        val id = foto.id
        if (id == null) {
            // Nothing to delete on server; just close and notify caller to refresh
            dismiss()
            onDeleted()
            return
        }

        RetrofitClient.instance.eliminarFoto(id)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    dismiss()
                    onDeleted()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {}
            })
    }
}

