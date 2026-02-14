package pt.ipt.dam2025.memories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
/**Retrofit: Biblioteca para tornar a API em uma interface de Kotlin/Java. https://square.github.io/retrofit/ */

/** * Singleton para configurar e fornecer uma instância do Retrofit para chamadas à API. */
object RetrofitClient {
    /** * BASE_URL: URL base da API REST. . */
     private const val BASE_URL = "http://143.47.51.101:5000"

/** instance: Instância única do Retrofit configurada com a URL base e o conversor Gson para serialização/deserialização de JSON. */
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}