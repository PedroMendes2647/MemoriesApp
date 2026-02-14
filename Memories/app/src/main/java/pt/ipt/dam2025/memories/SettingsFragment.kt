package pt.ipt.dam2025.memories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import pt.ipt.dam2025.memories.databinding.FragmentSettingsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Setup logout button
        binding.logoutButton.setOnClickListener {
            logout()
        }

        // Setup delete account button
        binding.deleteAccountButton.setOnClickListener {
            showDeleteAccountConfirmation()
        }

        return binding.root
    }

    private fun logout() {
        // Clear any stored user data if needed
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    private fun showDeleteAccountConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Apagar Conta")
            .setMessage("Tem a certeza que deseja apagar a sua conta? Esta ação é irreversível e todos os seus dados serão eliminados.")
            .setPositiveButton("Apagar") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteAccount() {
        // Get user ID from activity intent
        val userId = activity?.intent?.getIntExtra("USER_ID", -1) ?: -1

        if (userId == -1) {
            Toast.makeText(context, "Erro: ID de utilizador não encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        // Show loading message
        Toast.makeText(context, "A apagar conta...", Toast.LENGTH_SHORT).show()

        // Call API to delete account
        RetrofitClient.instance.deleteAccount(userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Conta apagada com sucesso", Toast.LENGTH_LONG).show()
                    // Redirect to login screen
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Toast.makeText(context, "Erro ao apagar conta: ${response.code()}", Toast.LENGTH_SHORT).show()
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

