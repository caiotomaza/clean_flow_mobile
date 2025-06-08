package com.unifapce.clean_flow.data

import com.google.gson.annotations.SerializedName

data class Filial(
    @SerializedName("id") val id: Int,
    @SerializedName("nome") val nome: String
)