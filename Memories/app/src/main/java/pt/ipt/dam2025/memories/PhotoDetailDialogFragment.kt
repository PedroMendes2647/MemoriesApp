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

class PhotoDetailDialogFragment(
    private val foto: Foto,
    private val onDeleted: () -> Unit
) : DialogFragment() {

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

