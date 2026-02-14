package pt.ipt.dam2025.memories
/** * Modelos de dados para as requisições e respostas da API. */
/**
 * RegisterRequest: Representa os dados necessários para registrar um novo utilizador.
 * LoginRequest: Representa os dados necessários para autenticar um utilizador.
 * LoginResponse: Representa a resposta da API após uma tentativa de login, contendo o token de autenticação e o ID do usuário.
 * Foto: Representa os dados de uma foto, incluindo informações como id da imagem, id do usuário, descrição, localização (latitude e longitude) e o caminho da imagem.
 */
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
