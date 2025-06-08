package com.unifapce.clean_flow // Altere para o seu pacote

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.unifapce.clean_flow.databinding.ActivityLoginBinding // Importe a classe de binding gerada

class LoginActivity : AppCompatActivity() {

    // Declara a variável para o View Binding
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla o layout usando o View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o clique do botão
        binding.buttonLogin.setOnClickListener {
            // Nenhuma validação é feita aqui
            abrirTelaPrincipal()
        }
    }

    private fun abrirTelaPrincipal() {
        // Cria uma Intent para iniciar a HomeActivity
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        // Opcional: Finaliza a LoginActivity para que o usuário não possa voltar a ela pressionando o botão "Voltar"
        finish()
    }
}