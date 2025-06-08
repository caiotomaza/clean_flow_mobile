package com.unifapce.clean_flow.data

import com.google.gson.annotations.SerializedName

data class Veiculo(
    @SerializedName("placa") val placa: String
)