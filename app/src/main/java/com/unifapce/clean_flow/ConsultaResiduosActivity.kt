package com.unifapce.clean_flow

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unifapce.clean_flow.adapter.ConsultaAdapter
import com.unifapce.clean_flow.data.ConsultaArmazenamento
import com.unifapce.clean_flow.data.ConsultaEntrada
import com.unifapce.clean_flow.data.ConsultaSaida
import com.unifapce.clean_flow.network.ApiService
import com.unifapce.clean_flow.network.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ConsultaResiduosActivity : AppCompatActivity() {

    private lateinit var rvEntradas: RecyclerView
    private lateinit var rvSaidas: RecyclerView
    private lateinit var rvArmazenamento: RecyclerView
    private lateinit var btnVoltar: Button

    private lateinit var entradasAdapter: ConsultaAdapter
    private lateinit var saidasAdapter: ConsultaAdapter
    private lateinit var armazenamentoAdapter: ConsultaAdapter

    private val apiService: ApiService by lazy {
        RetrofitClient.apiService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta_residuos)

        // Inicializa as Views do layout
        rvEntradas = findViewById(R.id.rv_entradas)
        rvSaidas = findViewById(R.id.rv_saidas)
        rvArmazenamento = findViewById(R.id.rv_armazenamento)
        btnVoltar = findViewById(R.id.btn_voltar)

        setupRecyclerViews()

        btnVoltar.setOnClickListener {
            finish()
        }

        buscarTodosOsDados()
    }

    private fun setupRecyclerViews() {
        // Configura RecyclerView de Entradas
        entradasAdapter = ConsultaAdapter()
        rvEntradas.layoutManager = LinearLayoutManager(this)
        rvEntradas.adapter = entradasAdapter

        // Configura RecyclerView de Saídas
        saidasAdapter = ConsultaAdapter()
        rvSaidas.layoutManager = LinearLayoutManager(this)
        rvSaidas.adapter = saidasAdapter

        // Configura RecyclerView de Armazenamento
        armazenamentoAdapter = ConsultaAdapter()
        rvArmazenamento.layoutManager = LinearLayoutManager(this)
        rvArmazenamento.adapter = armazenamentoAdapter
    }

    private fun buscarTodosOsDados() {
        lifecycleScope.launch {
            try {
                // Inicia as 3 chamadas de API em paralelo para otimizar
                val entradasDeferred = async { apiService.getConsultasEntradas() }
                val saidasDeferred = async { apiService.getConsultasSaidas() }
                val armazenamentosDeferred = async { apiService.getConsultasArmazenamentos() }

                // Aguarda o resultado de todas
                val responseEntradas = entradasDeferred.await()
                val responseSaidas = saidasDeferred.await()
                val responseArmazenamentos = armazenamentosDeferred.await()

                if (responseEntradas.isSuccessful && responseSaidas.isSuccessful && responseArmazenamentos.isSuccessful) {
                    atualizarUI(
                        entradas = responseEntradas.body().orEmpty(),
                        saidas = responseSaidas.body().orEmpty(),
                        armazenamentos = responseArmazenamentos.body().orEmpty()
                    )
                } else {
                    Toast.makeText(this@ConsultaResiduosActivity, "Erro ao carregar os registros.", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@ConsultaResiduosActivity, "Falha na conexão: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    private fun atualizarUI(
        entradas: List<ConsultaEntrada>,
        saidas: List<ConsultaSaida>,
        armazenamentos: List<ConsultaArmazenamento>
    ) {
        // Atualiza os adapters com os novos dados
        entradasAdapter.updateData(entradas)
        saidasAdapter.updateData(saidas)
        armazenamentoAdapter.updateData(armazenamentos)
    }
}
