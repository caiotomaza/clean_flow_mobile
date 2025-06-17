# Clean Flow (Mobile) 📱

## 📝 Descrição

Este projeto é um aplicativo Android nativo desenvolvido em **Kotlin** que consome uma API REST para a aplicação Clean Flow. A comunicação com a rede é gerenciada pela biblioteca **Retrofit** e a serialização/desserialização de objetos JSON é feita com **Gson**.


## ✨ Funcionalidades

* Registrar entrda de residuos solidos;
* Registrar saida de residuos solidos;
* Realizar consulta de entrada, saida e armazenamento.


## 🛠️ Tecnologias Utilizadas

Este projeto foi construído utilizando as seguintes tecnologias e bibliotecas:

* **[Kotlin](https://kotlinlang.org/):** Linguagem de programação oficial para desenvolvimento Android.
* **[Retrofit 2](https://square.github.io/retrofit/):** Cliente HTTP type-safe para Android e Java.
* **[Gson](https://github.com/google/gson):** Biblioteca para converter objetos Java (e Kotlin) para sua representação JSON e vice-versa.
* **[OkHttp 3](https://square.github.io/okhttp/):** Cliente HTTP eficiente, usado por baixo dos panos pelo Retrofit.
* **[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel):** Para armazenar e gerenciar dados relacionados à UI de forma consciente do ciclo de vida.
* **[LiveData](https://developer.android.com/topic/libraries/architecture/livedata):** Para notificar as views sobre mudanças na base de dados de forma observável.


## 🚀 Como Executar o Projeto

Para executar este projeto, você precisará do [Android Studio](https://developer.android.com/studio) instalado. Siga os passos abaixo:

1.  Clone o repositório;

2.  Abra o projeto no Android Studio;

3. Aguarde o Grade sincronizar;

4. E so executar o projeto ☺️.


## ⚙️ Configuração da API (Retrofit)

A configuração do Retrofit pode ser encontrada no pacote `[com.unifapce.clean_flow.network]`.

### 🛜 Interface da API

A interface `ApiService.kt` define todos os endpoints da API:

```kotlin
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

    @POST("mobile/entrada/store")
    suspend fun registrarEntrada(@Body request: RegistroResiduoRequest): Response<ApiResponse>

    @POST("mobile/saida/store")
    suspend fun registrarSaida(@Body request: RegistroSaidaRequest): Response<ApiResponse>

    @GET("mobile/entradas")
    suspend fun getConsultasEntradas(): Response<List<ConsultaEntrada>>

    @GET("mobile/saidas")
    suspend fun getConsultasSaidas(): Response<List<ConsultaSaida>>

    @GET("mobile/armazenamentos")
    suspend fun getConsultasArmazenamentos(): Response<List<ConsultaArmazenamento>>
}
```