// Em app/src/main/java/com/unifapce/clean_flow/data/ConsultaEntrada.kt
package com.unifapce.clean_flow.data

import com.google.gson.annotations.SerializedName

// Representa um registro de ENTRADA retornado pelo backend
data class ConsultaEntrada(
    @SerializedName("id") val id: Int,
    @SerializedName("placa_veiculo") val placaVeiculo: String?,
    @SerializedName("material_nome") val materialNome: String?, // Nome do material
    @SerializedName("peso_inicial") val pesoInicial: String?,
    @SerializedName("data_hora_entrada") val dataHora: String // Data formatada
)