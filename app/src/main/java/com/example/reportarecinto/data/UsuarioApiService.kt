package com.example.reportarecinto.data

import com.example.reportarecinto.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface UsuarioApiService {

    @GET("api/usuarios")
    suspend fun obtenerUsuarios(): List<Usuario>

    @POST("api/usuarios")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @POST("api/usuarios/login")
    suspend fun login(@Body credenciales: Map<String, String>): Response<Usuario>
}