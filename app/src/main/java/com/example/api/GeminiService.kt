package com.example.api

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class Part(
    @Json(name = "text") val text: String? = null
)

data class Content(
    @Json(name = "parts") val parts: List<Part>
)

data class GenerateContentRequest(
    @Json(name = "contents") val contents: List<Content>,
    @Json(name = "systemInstruction") val systemInstruction: Content? = null
)

data class Candidate(
    @Json(name = "content") val content: Content?
)

data class GenerateContentResponse(
    @Json(name = "candidates") val candidates: List<Candidate>?
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service: GeminiApiService = retrofit.create(GeminiApiService::class.java)

    suspend fun chatWithGemini(userMessage: String, history: List<Content>): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return "FALLO_DE_SISTEMA: Clave API de Gemini ausente. Por favor, añádela en el panel de Secretos."
        }

        val systemInstruction = Content(
            parts = listOf(
                Part(
                    text = "Eres la Inteligencia Artificial secreta integrada en la terminal XL7 de Gamersito. " +
                            "Tu tono de respuesta debe ser formal, técnico, altamente minimalista, serio y con toques de hackeo cyberpunk. " +
                            "Responde siempre en español. Mantén respuestas concisas, contundentes y de alto valor estratégico. " +
                            "Usa términos de red como 'Terminal XL7', 'Núcleo Central', 'Protocolo de Seguridad', 'Comunidad Gamer' si es relevante. " +
                            "Ayuda al usuario a estructurar ideas, alcanzar metas, escribir proyectos o planificar disciplina, de forma rápida y concisa."
                )
            )
        )

        val fullContents = history + Content(parts = listOf(Part(text = userMessage)))

        val request = GenerateContentRequest(
            contents = fullContents,
            systemInstruction = systemInstruction
        )

        return try {
            val response = service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "SISTEMA_ERROR: No se pudo descifrar la respuesta."
        } catch (e: Exception) {
            e.printStackTrace()
            "CONEXION_PERDIDA: Consola fuera de rango. (${e.localizedMessage ?: "Verifique conexión"})"
        }
    }
}
