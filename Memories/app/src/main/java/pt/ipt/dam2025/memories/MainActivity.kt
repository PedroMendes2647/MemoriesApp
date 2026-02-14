package pt.ipt.dam2025.memories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dam2025.memories.databinding.ActivityMainBinding
/** * Atividade principal que hospeda os fragmentos de galeria, câmera, configurações e sobre. */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

/** * Configura a navegação entre os fragmentos usando o BottomNavigationView e define o fragmento de galeria como padrão. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


// Default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, GalleryFragment())
            .commit()


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_gallery -> GalleryFragment()
                R.id.nav_camera -> CameraFragment()
                R.id.nav_settings -> SettingsFragment()
                R.id.nav_about -> AboutFragment()
                else -> null
            }


            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, it)
                    .commit()
            }
            true
        }
    }
}