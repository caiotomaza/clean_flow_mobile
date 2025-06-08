package com.unifapce.clean_flow.data

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("errors") val errors: Map<String, List<String>>? // Para erros de validação
)