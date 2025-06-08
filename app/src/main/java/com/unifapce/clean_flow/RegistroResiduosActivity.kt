package com.unifapce.clean_flow

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.unifapce.clean_flow.network.RetrofitClient
import com.unifapce.clean_flow.data.* // Importar todas as classes de dados
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistroResiduosActivity : AppCompatActivity() {

    private lateinit var spinnerFilial: Spinner
    private lateinit var spinnerPlacaVeiculo: Spinner
    private lateinit var etPesoInicial: EditText
    private lateinit var spinnerMaterial: Spinner
    private lateinit var spinnerSubtipoMaterial: Spinner
    private lateinit var spinnerResponsavel: Spinner
    private lateinit var etIdContainer: EditText
    private lateinit var etDataArmazenamento: EditText
    private lateinit var btnConcluir: Button
    private lateinit var btnCancelar: Button

    private var selectedFilialId: Int? = null
    private var selectedPlacaVeiculo: String? = null
    private var selectedMaterialId: Int? = null
    private var selectedSubtipoMaterialId: Int? = null
    private var selectedResponsavelId: Int? = null

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_residuos)

        // Inicializar Views
        spinnerFilial = findViewById(R.id.spinnerFilial)
        spinnerPlacaVeiculo = findViewById(R.id.spinnerPlacaVeiculo)
        etPesoInicial = findViewById(R.id.etPesoInicial)
        spinnerMaterial = findViewById(R.id.spinnerMaterial)
        spinnerSubtipoMaterial = findViewById(R.id.spinnerSubtipoMaterial)
        spinnerResponsavel = findViewById(R.id.spinnerResponsavel)
        etIdContainer = findViewById(R.id.etIdContainer)
        etDataArmazenamento = findViewById(R.id.etDataArmazenamento)
        btnConcluir = findViewById(R.id.btnConcluir)
        btnCancelar = findViewById(R.id.btnCancelar)

        // Carregar dados para os Spinners
        loadSpinnerData()

        // Configurar Date/Time Picker para etDataArmazenamento
        etDataArmazenamento.setOnClickListener {
            showDateTimePicker()
        }

        // Ação do botão Concluir
        btnConcluir.setOnClickListener {
            enviarRegistro()
        }

        // Ação do botão Cancelar (ex: fechar a Activity)
        btnCancelar.setOnClickListener {
            finish() // Fecha a Activity
        }
    }

    private fun loadSpinnerData() {
        lifecycleScope.launch {
            try {
                // Filiais
                val filiaisResponse = RetrofitClient.apiService.getFiliais()
                if (filiaisResponse.isSuccessful && filiaisResponse.body() != null) {
                    val filiais = filiaisResponse.body()!!
                    val filialNames = filiais.map { it.nome }
                    val adapter = ArrayAdapter(this@RegistroResiduosActivity, android.R.layout.simple_spinner_item, listOf("Selecione") + filialNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerFilial.adapter = adapter
                    spinnerFilial.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selectedFilialId = if (position > 0) filiais[position - 1].id else null
                        }
                        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) { selectedFilialId = null }
                    }
                } else {
                    Log.e("API_CALL", "Erro ao carregar filiais: ${filiaisResponse.code()} - ${filiaisResponse.errorBody()?.string()}")
                    Toast.makeText(this@RegistroResiduosActivity, "Erro ao carregar filiais.", Toast.LENGTH_SHORT).show()
                }

                // Veículos
                val veiculosResponse = RetrofitClient.apiService.getVeiculos()
                if (veiculosResponse.isSuccessful && veiculosResponse.body() != null) {
                    val veiculos = veiculosResponse.body()!!
                    val placas = veiculos.map { it.placa }
                    val adapter = ArrayAdapter(this@RegistroResiduosActivity, android.R.layout.simple_spinner_item, listOf("Selecione") + placas)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerPlacaVeiculo.adapter = adapter
                    spinnerPlacaVeiculo.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selectedPlacaVeiculo = if (position > 0) placas[position - 1] else null
                        }
                        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) { selectedPlacaVeiculo = null }
                    }
                } else {
                    Log.e("API_CALL", "Erro ao carregar veículos: ${veiculosResponse.code()} - ${veiculosResponse.errorBody()?.string()}")
                    Toast.makeText(this@RegistroResiduosActivity, "Erro ao carregar veículos.", Toast.LENGTH_SHORT).show()
                }

                // Resíduos
                val residuosResponse = RetrofitClient.apiService.getResiduos()
                if (residuosResponse.isSuccessful && residuosResponse.body() != null) {
                    val residuos = residuosResponse.body()!!
                    val residuoNames = residuos.map { it.nome }
                    val adapter = ArrayAdapter(this@RegistroResiduosActivity, android.R.layout.simple_spinner_item, listOf("Selecione") + residuoNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerMaterial.adapter = adapter
                    spinnerMaterial.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selectedMaterialId = if (position > 0) residuos[position - 1].id else null
                        }
                        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) { selectedMaterialId = null }
                    }
                } else {
                    Log.e("API_CALL", "Erro ao carregar resíduos: ${residuosResponse.code()} - ${residuosResponse.errorBody()?.string()}")
                    Toast.makeText(this@RegistroResiduosActivity, "Erro ao carregar resíduos.", Toast.LENGTH_SHORT).show()
                }

                // Subtipos de Resíduos
                val subResiduosResponse = RetrofitClient.apiService.getSubResiduos()
                if (subResiduosResponse.isSuccessful && subResiduosResponse.body() != null) {
                    val subResiduos = subResiduosResponse.body()!!
                    val subResiduoNames = subResiduos.map { it.nome }
                    val adapter = ArrayAdapter(this@RegistroResiduosActivity, android.R.layout.simple_spinner_item, listOf("Selecione") + subResiduoNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerSubtipoMaterial.adapter = adapter
                    spinnerSubtipoMaterial.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selectedSubtipoMaterialId = if (position > 0) subResiduos[position - 1].id else null
                        }
                        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) { selectedSubtipoMaterialId = null }
                    }
                } else {
                    Log.e("API_CALL", "Erro ao carregar subtipos: ${subResiduosResponse.code()} - ${subResiduosResponse.errorBody()?.string()}")
                    Toast.makeText(this@RegistroResiduosActivity, "Erro ao carregar subtipos.", Toast.LENGTH_SHORT).show()
                }

                // Usuários (Responsáveis)
                val usuariosResponse = RetrofitClient.apiService.getUsuarios()
                if (usuariosResponse.isSuccessful && usuariosResponse.body() != null) {
                    val usuarios = usuariosResponse.body()!!
                    val userNames = usuarios.map { it.name }
                    val adapter = ArrayAdapter(this@RegistroResiduosActivity, android.R.layout.simple_spinner_item, listOf("Selecione") + userNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerResponsavel.adapter = adapter
                    spinnerResponsavel.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selectedResponsavelId = if (position > 0) usuarios[position - 1].id else null
                        }
                        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) { selectedResponsavelId = null }
                    }
                } else {
                    Log.e("API_CALL", "Erro ao carregar usuários: ${usuariosResponse.code()} - ${usuariosResponse.errorBody()?.string()}")
                    Toast.makeText(this@RegistroResiduosActivity, "Erro ao carregar usuários.", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("API_CALL", "Exceção ao carregar dados dos spinners: ${e.message}", e)
                Toast.makeText(this@RegistroResiduosActivity, "Erro de rede ao carregar dados.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDateTimePicker() {
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        updateDateTimeEditText()
                    },
                    currentHour,
                    currentMinute,
                    true
                ).show()
            },
            currentYear,
            currentMonth,
            currentDay
        ).show()
    }

    private fun updateDateTimeEditText() {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
        etDataArmazenamento.setText(format.format(calendar.time))
    }

    private fun enviarRegistro() {
        // Validação básica dos campos
        if (selectedFilialId == null) {
            Toast.makeText(this, "Selecione uma Filial.", Toast.LENGTH_SHORT).show()
            return
        }
        if (etPesoInicial.text.isBlank()) {
            Toast.makeText(this, "Informe o Peso.", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedMaterialId == null) {
            Toast.makeText(this, "Selecione o Tipo de Resíduo.", Toast.LENGTH_SHORT).show()
            return
        }
        // Validações para os outros campos conforme sua necessidade...
        if (etDataArmazenamento.text.isBlank()) {
            Toast.makeText(this, "Informe a Data e Hora da Entrada.", Toast.LENGTH_SHORT).show()
            return
        }

        // Formatar data e hora para ISO 8601 (YYYY-MM-DDTHH:MM:SS) para o backend
        val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) // Use Locale.US para garantir formato sem variação
        val dataArmazenamentoFormatted = isoDateFormat.format(calendar.time)

        val request = RegistroResiduoRequest(
            idFilial = selectedFilialId,
            placaVeiculo = selectedPlacaVeiculo,
            pesoInicial = etPesoInicial.text.toString(),
            idMaterial = selectedMaterialId,
            idSubtituloMaterial = selectedSubtipoMaterialId,
            idResponsavel = selectedResponsavelId,
            idContainer = etIdContainer.text.toString().takeIf { it.isNotBlank() }, // Envia null se estiver vazio
            dataArmazenamento = dataArmazenamentoFormatted
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.registrarEntrada(request)

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.status == "success") {
                        Toast.makeText(this@RegistroResiduosActivity, apiResponse.message, Toast.LENGTH_LONG).show()
                        finish() // Opcional: fechar a activity após o sucesso
                    } else {
                        // Tratar erros de validação ou outros erros de negócio do backend
                        val errorMessage = apiResponse.message ?: "Erro desconhecido."
                        val errorDetails = apiResponse.errors?.entries?.joinToString("\n") { (field, errors) ->
                            "$field: ${errors.joinToString(", ")}"
                        }
                        val fullMessage = if (errorDetails != null) "$errorMessage\n$errorDetails" else errorMessage
                        Toast.makeText(this@RegistroResiduosActivity, fullMessage, Toast.LENGTH_LONG).show()
                        Log.e("API_SUBMIT", "Erro no registro: ${apiResponse.status} - $errorMessage\n$errorDetails")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@RegistroResiduosActivity, "Erro na submissão: ${response.code()} - $errorBody", Toast.LENGTH_LONG).show()
                    Log.e("API_SUBMIT", "Erro na resposta HTTP: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegistroResiduosActivity, "Erro de conexão: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("API_SUBMIT", "Exceção ao enviar registro: ${e.message}", e)
            }
        }
    }
}