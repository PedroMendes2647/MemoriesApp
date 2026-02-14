package pt.ipt.dam2025.memories

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<Map<String, String>>

    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/fotos/{user_id}")
    fun listarFotos(@Path("user_id") userId: Int): Call<List<Foto>>

    @DELETE("api/fotos/{id}")
    fun eliminarFoto(@Path("id") id: Int): Call<Void>
}