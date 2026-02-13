package pt.ipt.dam2025.memories

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import pt.ipt.dam2025.memories.databinding.FragmentCameraBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    // Cliente para GPS
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    // --- LANÇADORES DE RESULTADOS (Launchers) ---

    // 1. Recebe a FOTO da Câmara
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            binding.imgPreview.setImageBitmap(bitmap)
            mostrarFormulario(true)
        }
    }

    // 2. Recebe a IMAGEM da Galeria
    private val pickGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            binding.imgPreview.setImageURI(uri)
            mostrarFormulario(true)
        }
    }

    // 3. Permissões para CÂMARA e GPS
    private val requestCameraLocationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.CAMERA] == true &&
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            takePictureLauncher.launch(null)
        } else {
            Toast.makeText(context, "Permissões de Câmara e GPS necessárias", Toast.LENGTH_SHORT).show()
        }
    }

    // 4. Permissão para GALERIA (NOVO)
    private val requestGalleryPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickGalleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permissão negada para aceder à galeria", Toast.LENGTH_SHORT).show()
        }
    }

    // --- CICLO DE VIDA ---

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        // Botão Câmara: Pede permissão e abre câmara
        binding.btnAbrirCamara.setOnClickListener {
            verificarPermissoesCamaraGPS()
        }

        // Botão Galeria: Pede permissão e abre galeria
        binding.btnAbrirGaleria.setOnClickListener {
            verificarPermissoesGaleria()
        }

        // Botão Cancelar (dentro do form)
        binding.btnCancelarSelecao.setOnClickListener {
            mostrarFormulario(false)
        }

        // Botão Guardar
        binding.btnGuardar.setOnClickListener {
            enviarDados()
        }



        return binding.root
    }

    // --- FUNÇÕES AUXILIARES ---

    private fun verificarPermissoesCamaraGPS() {
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissions.all { ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED }) {
            takePictureLauncher.launch(null)
        } else {
            requestCameraLocationLauncher.launch(permissions)
        }
    }

    private fun verificarPermissoesGaleria() {
        // Lógica para Android 13 (Tiramisu) vs Versões Antigas
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            pickGalleryLauncher.launch("image/*")
        } else {
            requestGalleryPermissionLauncher.launch(permission)
        }
    }

    private fun mostrarFormulario(mostrar: Boolean) {
        if (mostrar) {
            binding.botoesContainer.visibility = View.GONE
            binding.txtInstrucao.visibility = View.GONE
            binding.formContainer.visibility = View.VISIBLE
            binding.imgPreview.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
        } else {
            binding.botoesContainer.visibility = View.VISIBLE
            binding.txtInstrucao.visibility = View.VISIBLE
            binding.formContainer.visibility = View.GONE
            binding.imgPreview.setImageResource(android.R.drawable.ic_menu_gallery)
            binding.imgPreview.scaleType = android.widget.ImageView.ScaleType.CENTER_INSIDE
            binding.editDescricao.text.clear() // Limpa o texto ao cancelar
        }
    }

    // Função chamada pelo botão Guardar
    private fun enviarDados() {
        // 1. Validar APENAS a descrição
        val descricao = binding.editDescricao.text.toString().trim()

        if (descricao.isEmpty()) {
            binding.editDescricao.error = "A descrição é obrigatória"
            return
        }

        // 2. Obter User ID
        val userId = activity?.intent?.getIntExtra("USER_ID", 1) ?: 1

        // 3. Obter GPS (com proteção para PC/Emulador)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    // GPS Real
                    enviarParaAPI(userId, descricao, location.latitude, location.longitude)
                } else {
                    // Sem GPS (PC) -> Usa Leiria
                    Toast.makeText(context, "Sem GPS. A usar Leiria.", Toast.LENGTH_SHORT).show()
                    enviarParaAPI(userId, descricao, 39.734685, -8.820860)
                }
            }.addOnFailureListener {
                enviarParaAPI(userId, descricao, 39.734685, -8.820860)
            }
        } catch (e: SecurityException) {
            enviarParaAPI(userId, descricao, 39.734685, -8.820860)
        }
    }

    // Função que envia para a API
    private fun enviarParaAPI(iduser: Int, desc: String, lat: Double, lon: Double) {

        // Cria o objeto JSON (Sem título)
        val fotoReq = Foto(
            user_id = iduser,
            descricao = desc,
            latitude = lat,
            longitude = lon
        )

        RetrofitClient.instance.guardarFoto(fotoReq).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Guardado com sucesso!", Toast.LENGTH_LONG).show()
                    binding.editDescricao.text.clear()
                    mostrarFormulario(false)
                } else {
                    Toast.makeText(context, "Erro API: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Erro de rede: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}