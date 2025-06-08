package com.unifapce.clean_flow.data

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String // Assumindo 'name' no JSON para user->name
)
