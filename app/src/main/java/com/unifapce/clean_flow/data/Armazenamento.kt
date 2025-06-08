package com.unifapce.clean_flow.data

import com.google.gson.annotations.SerializedName

data class Armazenamento(
    @SerializedName("id_arm")
    val id_arm: Int,

    // O nome pode ser nulo, como visto no seu código Blade
    @SerializedName("container")
    val nome: String?
)