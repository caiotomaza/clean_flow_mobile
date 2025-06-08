package com.unifapce.clean_flow.data

import com.google.gson.annotations.SerializedName

data class RegistroResiduoRequest(
    @SerializedName("tipo_registro") val tipoRegistro: String = "entrada", // Valor fixo
    @SerializedName("id_filial_input") val idFilial: Int?,
    @SerializedName("placa_veiculo") val placaVeiculo: String?,
    @SerializedName("peso_inicial") val pesoInicial: String?, // Mantido como String para flexibilidade com "kg"
    @SerializedName("material") val idMaterial: Int?,
    @SerializedName("subtitulo_material") val idSubtituloMaterial: Int?,
    @SerializedName("id_responsavel") val idResponsavel: Int?,
    @SerializedName("id_container") val idContainer: String?,
    @SerializedName("data_armazenamento") val dataArmazenamento: String? // Formato ISO 8601
)