package pt.ipt.dam2025.memories

data class RegisterRequest(
    val username: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user_id: Int
)

data class Foto(
    val id: Int? = null,
    val user_id: Int,
    val descricao: String,
    val latitude: Double,
    val longitude: Double,
    val imagem: String? = null
)
