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
    val id: Int,
    val descricao: String,
    val lat: Double,
    val lon: Double,
    val imagem: String
)