package com.unifapce.clean_flow // Altere para o seu pacote

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.unifapce.clean_flow.data.*
import com.unifapce.clean_flow.databinding.ActivityRegistroSaidaBinding
import com.unifapce.clean_flow.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class RegistroSaidaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroSaidaBinding

    // Listas para os dados dos dropdowns
    private var filiais: List<Filial> = emptyList()
    private var armazenamentos: List<Armazenamento> = emptyList()
    private var veiculos: List<Veiculo> = emptyList()

    // Variáveis para os valores selecionados
    private var selectedFilialId: Int? = null
    private var selectedArmazenamentoId: Int? = null
    private var selectedPlaca: String? = null

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroSaidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadInitialData()
        setupClickListeners()
    }

    private fun loadInitialData() {
        // Usando Coroutines para carregar dados da rede em background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                filiais = RetrofitClient.apiService.getFiliais().body() ?: emptyList()
                armazenamentos = RetrofitClient.apiService.getArmazenamentos().body() ?: emptyList()
                veiculos = RetrofitClient.apiService.getVeiculos().body() ?: emptyList()

                // Volta para a thread principal para atualizar a UI
                withContext(Dispatchers.Main) {
                    setupDropdowns()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegistroSaidaActivity, "Erro ao carregar dados: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupDropdowns() {
        // Filiais
        val filialAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, filiais.map { it.nome })
        binding.autocompleteFilial.setAdapter(filialAdapter)
        binding.autocompleteFilial.setOnItemClickListener { _, _, position, _ ->
            selectedFilialId = filiais[position].id
        }

        // Armazenamentos
        val armAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, armazenamentos.map { it.nome ?: "Armazenamento #${it.id_arm}" })
        binding.autocompleteArmazenamento.setAdapter(armAdapter)
        binding.autocompleteArmazenamento.setOnItemClickListener { _, _, position, _ ->
            selectedArmazenamentoId = armazenamentos[position].id_arm
        }

        // Veículos
        val veiculoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, veiculos.map { it.placa })
        binding.autocompleteVeiculo.setAdapter(veiculoAdapter)
        binding.autocompleteVeiculo.setOnItemClickListener { _, _, position, _ ->
            selectedPlaca = veiculos[position].placa
        }
    }

    private fun setupClickListeners() {
        binding.editTextData.setOnClickListener { showDateTimePicker() }
        binding.buttonSalvar.setOnClickListener { submitForm() }
    }

    private fun showDateTimePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                updateDataLabel()
            }
            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }
        DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateDataLabel() {
        val displayFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        binding.editTextData.setText(displayFormat.format(calendar.time))
    }

    private fun submitForm() {
        val idSaidaStr = binding.editTextIdSaida.text.toString()
        val dataStr = binding.editTextData.text.toString()

        if (idSaidaStr.isBlank() || selectedFilialId == null || selectedArmazenamentoId == null || selectedPlaca == null || dataStr.isBlank()) {
            Toast.makeText(this, "Por favor, preencha todos os campos obrigatórios (*)", Toast.LENGTH_LONG).show()
            return
        }

        val backendFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dataParaBackend = backendFormat.format(calendar.time)

        val request = RegistroSaidaRequest(
            idSaida = idSaidaStr.toInt(),
            idFilial = selectedFilialId!!,
            idArmazenamento = selectedArmazenamentoId!!,
            placaVeiculo = selectedPlaca!!,
            dataHora = dataParaBackend
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.registrarSaida(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegistroSaidaActivity, "Saída registrada com sucesso!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegistroSaidaActivity, "Erro ao registrar: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegistroSaidaActivity, "Falha na comunicação: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}