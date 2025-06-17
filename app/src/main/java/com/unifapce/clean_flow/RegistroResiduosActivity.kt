package com.unifapce.clean_flow

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.unifapce.clean_flow.data.*
import com.unifapce.clean_flow.databinding.ActivityRegistroResiduosBinding // Importar a classe de binding gerada
import com.unifapce.clean_flow.network.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistroResiduosActivity : AppCompatActivity() {

    // Usar ViewBinding para acessar as views de forma segura
    private lateinit var binding: ActivityRegistroResiduosBinding

    // Listas para armazenar os dados dos dropdowns
    private var filiais: List<Filial> = emptyList()
    private var veiculos: List<Veiculo> = emptyList()
    private var residuos: List<Residuo> = emptyList()
    private var subResiduos: List<SubResiduo> = emptyList()
    private var usuarios: List<Usuario> = emptyList()

    // Variáveis para armazenar os IDs selecionados
    private var selectedFilialId: Int? = null
    private var selectedPlacaVeiculo: String? = null
    private var selectedMaterialId: Int? = null
    private var selectedSubtipoMaterialId: Int? = null
    private var selectedResponsavelId: Int? = null

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar o layout usando ViewBinding
        binding = ActivityRegistroResiduosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadDropdownData()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Configurar Date/Time Picker
        binding.editTextData.setOnClickListener {
            showDateTimePicker()
        }

        // Ação do botão Salvar
        binding.buttonSalvar.setOnClickListener {
            enviarRegistro()
        }

        // Ação do botão Voltar
        binding.buttonVoltar.setOnClickListener {
            finish() // Fecha a Activity
        }
    }

    private fun loadDropdownData() {
        lifecycleScope.launch {
            try {
                // Filiais
                filiais = RetrofitClient.apiService.getFiliais().body() ?: emptyList()
                val filialAdapter = ArrayAdapter(this@RegistroResiduosActivity, android.R.layout.simple_dropdown_item_1line, filiais.map { it.nome })
                binding.autocompleteFilial.setAdapter(filialAdapter)
                binding.autocompleteFilial.setOnItemClickListener { _, _, position, _ ->
                    selectedFilialId = filiais[position].id
                }

                // Veículos
                veiculos = RetrofitClient.apiService.getVeiculos().body() ?: emptyList()
                val veiculoAdapter = ArrayAdapter(this@RegistroResiduosActivity, android.R.layout.simple_dropdown_item_1line, veiculos.map { it.placa })
                binding.autocompleteVeiculo.setAdapter(veiculoAdapter)
                binding.autocompleteVeiculo.setOnItemClickListener { _, _, position, _ ->
                    selectedPlacaVeiculo = veiculos[position].placa
                }

                // Resíduos (Materiais)
                residuos = RetrofitClient.apiService.getResiduos().body() ?: emptyList()
                val materialAdapter = ArrayAdapter(this@RegistroResiduosActivity, android.R.layout.simple_dropdown_item_1line, residuos.map { it.nome })
                binding.autocompleteMaterial.setAdapter(materialAdapter)
                binding.autocompleteMaterial.setOnItemClickListener { _, _, position, _ ->
                    selectedMaterialId = residuos[position].id
                }

                // Subtipos de Resíduos
                subResiduos = RetrofitClient.apiService.getSubResiduos().body() ?: emptyList()
                val subResiduoAdapter = ArrayAdapter(this@RegistroResiduosActivity, android.R.layout.simple_dropdown_item_1line, subResiduos.map { it.nome })
                binding.autocompleteSubtipoMaterial.setAdapter(subResiduoAdapter)
                binding.autocompleteSubtipoMaterial.setOnItemClickListener { _, _, position, _ ->
                    selectedSubtipoMaterialId = subResiduos[position].id
                }

                // Usuários (Responsáveis)
                usuarios = RetrofitClient.apiService.getUsuarios().body() ?: emptyList()
                val usuarioAdapter = ArrayAdapter(this@RegistroResiduosActivity, android.R.layout.simple_dropdown_item_1line, usuarios.map { it.name })
                binding.autocompleteResponsavel.setAdapter(usuarioAdapter)
                binding.autocompleteResponsavel.setOnItemClickListener { _, _, position, _ ->
                    selectedResponsavelId = usuarios[position].id
                }

            } catch (e: Exception) {
                Log.e("API_CALL", "Exceção ao carregar dados dos dropdowns: ${e.message}", e)
                Toast.makeText(this@RegistroResiduosActivity, "Erro de rede ao carregar dados.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDateTimePicker() {
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                updateDateTimeEditText()
            }, currentHour, currentMinute, true).show()

        }, currentYear, currentMonth, currentDay).show()
    }

    private fun updateDateTimeEditText() {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
        binding.editTextData.setText(format.format(calendar.time))
    }

    private fun enviarRegistro() {
        // Validação usando os componentes do binding
        if (selectedFilialId == null) {
            binding.layoutFilial.error = "Selecione uma Filial"
            return
        } else {
            binding.layoutFilial.error = null
        }

        if (binding.editTextPeso.text.toString().isBlank()) {
            binding.layoutPeso.error = "Informe o Peso"
            return
        } else {
            binding.layoutPeso.error = null
        }

        if (selectedMaterialId == null) {
            binding.layoutMaterial.error = "Selecione o Tipo de Resíduo"
            return
        } else {
            binding.layoutMaterial.error = null
        }

        if (binding.editTextData.text.toString().isBlank()) {
            binding.layoutData.error = "Informe a Data e Hora"
            return
        } else {
            binding.layoutData.error = null
        }

        // Formatar data e hora para o formato esperado pelo backend
        val backendFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dataArmazenamentoFormatted = backendFormat.format(calendar.time)

        val request = RegistroResiduoRequest(
            idFilial = selectedFilialId,
            placaVeiculo = selectedPlacaVeiculo,
            pesoInicial = binding.editTextPeso.text.toString(),
            idMaterial = selectedMaterialId,
            idSubtituloMaterial = selectedSubtipoMaterialId,
            idResponsavel = selectedResponsavelId,
            idContainer = binding.editTextIdContainer.text.toString().takeIf { it.isNotBlank() },
            dataArmazenamento = dataArmazenamentoFormatted
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.registrarEntrada(request)

                if (response.isSuccessful) {
                    Toast.makeText(this@RegistroResiduosActivity, response.body()?.message ?: "Registrado com sucesso!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Erro desconhecido"
                    Toast.makeText(this@RegistroResiduosActivity, "Erro ao registrar: $errorMsg", Toast.LENGTH_LONG).show()
                    Log.e("API_SUBMIT", "Erro na resposta HTTP: ${response.code()} - $errorMsg")
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegistroResiduosActivity, "Erro de conexão: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("API_SUBMIT", "Exceção ao enviar registro: ${e.message}", e)
            }
        }
    }
}