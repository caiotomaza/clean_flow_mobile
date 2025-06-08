package com.unifapce.clean_flow.data

import com.google.gson.annotations.SerializedName

data class RegistroSaidaRequest(
    @SerializedName("id_saida")
    val idSaida: Int,

    @SerializedName("id_filial_sai")
    val idFilial: Int,

    @SerializedName("id_arm")
    val idArmazenamento: Int,

    // O formulário Blade envia a placa do veículo, então o tipo aqui é String
    @SerializedName("id_vec")
    val placaVeiculo: String,

    @SerializedName("data_hora")
    val dataHora: String
)