package com.unifapce.clean_flow

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Inicialize os botões
        val btnEntradaResiduos: Button = findViewById(R.id.btnEntradaResiduos)
        val btnSaidaResiduos: Button = findViewById(R.id.btnSaidaResiduos)
        val btnConsulta: Button = findViewById(R.id.btnConsulta)
        val btnDesconectar: Button = findViewById(R.id.btnDesconectar)

        // Configure os listeners para cada botão
        btnEntradaResiduos.setOnClickListener {
            val intent = Intent(this, RegistroResiduosActivity::class.java)
            startActivity(intent)
        }

        btnSaidaResiduos.setOnClickListener {
            val intent = Intent(this, RegistroSaidaActivity::class.java)
            startActivity(intent)
        }

        btnConsulta.setOnClickListener {
            val intent = Intent(this, ConsultaResiduosActivity::class.java)
            startActivity(intent)
        }

        // Listener para o botão Desconectar
        btnDesconectar.setOnClickListener {
            finish()
        }
    }
}