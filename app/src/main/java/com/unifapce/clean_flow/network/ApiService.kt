package com.unifapce.clean_flow.network

import com.unifapce.clean_flow.data.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("mobile/filial")
    suspend fun getFiliais(): Response<List<Filial>>

    @GET("mobile/veiculos")
    suspend fun getVeiculos(): Response<List<Veiculo>>

    @GET("mobile/residuos")
    suspend fun getResiduos(): Response<List<Residuo>>

    @GET("mobile/sub_residuos")
    suspend fun getSubResiduos(): Response<List<SubResiduo>>

    @GET("mobile/usuarios")
    suspend fun getUsuarios(): Response<List<Usuario>>

    @GET("mobile/armazenamento")
    suspend fun getArmazenamentos(): Response<List<Armazenamento>>

    // LINHA CORRIGIDA ABAIXO
    @POST("mobile/entrada/store")
    suspend fun registrarEntrada(@Body request: RegistroResiduoRequest): Response<ApiResponse>

    @POST("mobile/saida/store")
    suspend fun registrarSaida(@Body request: RegistroSaidaRequest): Response<ApiResponse>


    // --- ADICIONADO PARA A ConsultaResiduosActivity ---
    // Estes são os novos endpoints que a tela de consulta precisa.
    // Você precisará criá-los no seu backend (PHP/Laravel).

    @GET("mobile/entradas")
    suspend fun getConsultasEntradas(): Response<List<ConsultaEntrada>>

    @GET("mobile/saidas")
    suspend fun getConsultasSaidas(): Response<List<ConsultaSaida>>

    @GET("mobile/armazenamentos")
    suspend fun getConsultasArmazenamentos(): Response<List<ConsultaArmazenamento>>
}