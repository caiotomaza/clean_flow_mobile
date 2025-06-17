// Em app/src/main/java/com/unifapce/clean_flow/data/ConsultaArmazenamento.kt
package com.unifapce.clean_flow.data

import com.google.gson.annotations.SerializedName

// Representa um item em ARMAZENAMENTO retornado pelo backend
data class ConsultaArmazenamento(
    @SerializedName("id_arm") val id: Int,
    @SerializedName("container_nome") val containerNome: String?,
    @SerializedName("material_nome") val materialNome: String?,
    @SerializedName("peso_atual") val pesoAtual: String?,
    @SerializedName("data_ultima_movimentacao") val dataUltimaMovimentacao: String
)