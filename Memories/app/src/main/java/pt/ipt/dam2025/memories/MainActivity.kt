package pt.ipt.dam2025.memories

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pt.ipt.dam2025.memories.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


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