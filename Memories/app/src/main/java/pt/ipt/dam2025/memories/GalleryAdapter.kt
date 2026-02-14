package pt.ipt.dam2025.memories

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class GalleryAdapter(
    private val listaFotos: List<Foto>,
    private val onClick: (Foto) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageFoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listaFotos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foto = listaFotos[position]

        // Load image from phone path safely
        val path = foto.imagem
        Log.d("GalleryAdapter", "Loading image for position $position: path='$path'")

        if (path != null && path.isNotEmpty()) {
            val file = File(path)
            val exists = file.exists()
            Log.d("GalleryAdapter", "File exists: $exists for path: $path")

            if (exists) {
                holder.imageView.setImageURI(Uri.fromFile(file))
                Log.d("GalleryAdapter", "Successfully loaded image from: $path")
            } else {
                Log.w("GalleryAdapter", "File does not exist: $path")
                holder.imageView.setImageResource(R.drawable.placeholder) // fallback
            }
        } else {
            Log.w("GalleryAdapter", "Path is null or empty for position $position")
            holder.imageView.setImageResource(R.drawable.placeholder) // fallback
        }

        holder.itemView.setOnClickListener {
            onClick(foto)
        }
    }
}

