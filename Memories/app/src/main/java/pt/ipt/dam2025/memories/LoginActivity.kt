package pt.ipt.dam2025.memories

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dam2025.memories.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
/**Retrofit: Biblioteca para tornar a API em uma interface de Kotlin/Java. https://square.github.io/retrofit/ */

/** * Atividade de login e registo de utilizadores. */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
/** * Configura os listeners para os botões de login e registo, fazendo chamadas à API utilizando Retrofit. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // LOGIN
        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            val request = LoginRequest(username, password)

            RetrofitClient.instance.login(request)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val userId = response.body()?.user_id

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("USER_ID", userId)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login Errado", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "API error", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        // REGISTER
        binding.regButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            val request = RegisterRequest(username, password)

            RetrofitClient.instance.register(request)
                .enqueue(object : Callback<Map<String, String>> {
                    override fun onResponse(
                        call: Call<Map<String, String>>,
                        response: Response<Map<String, String>>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@LoginActivity, "Registado com sucesso", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginActivity, "Utilizador já existe", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "API error", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}