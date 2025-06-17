// Em app/src/main/java/com/unifapce/clean_flow/data/ConsultaSaida.kt
package com.unifapce.clean_flow.data

import com.google.gson.annotations.SerializedName

// Representa um registro de SAÍDA retornado pelo backend
data class ConsultaSaida(
    @SerializedName("id_saida") val id: Int,
    @SerializedName("placa_veiculo") val placaVeiculo: String?,
    @SerializedName("container_origem") val containerOrigem: String?, // Nome do container de onde saiu
    @SerializedName("data_hora_saida") val dataHora: String
)