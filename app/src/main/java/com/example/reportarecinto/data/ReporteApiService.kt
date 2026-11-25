package com.example.reportarecinto.data

import com.example.reportarecinto.model.Reporte
import retrofit2.Response
import retrofit2.http.*

interface ReporteApiService {

    @GET("api/reportes")
    suspend fun obtenerReportes(): List<Reporte>

    @POST("api/reportes")
    suspend fun crearReporte(@Body reporte: Reporte): Reporte

    @PUT("api/reportes/{id}")
    suspend fun actualizarReporte(
        @Path("id") id: String,
        @Body reporte: Reporte
    ): Response<Reporte>

    @DELETE("api/reportes/{id}")
    suspend fun eliminarReporte(@Path("id") id: String): Response<Unit>
}