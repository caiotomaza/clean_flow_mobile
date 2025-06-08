package com.unifapce.clean_flow.data

import com.google.gson.annotations.SerializedName

data class Residuo(
    @SerializedName("id") val id: Int, // Assumindo 'id' no JSON para id_resd
    @SerializedName("nome") val nome: String
)
