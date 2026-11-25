package com.example.reportarecinto.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.reportarecinto.model.Reporte

class ReporteDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "reporta_recinto.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_REPORTES = "reportes"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_REPORTES (
                id TEXT PRIMARY KEY,
                titulo TEXT,
                nombreUsuario TEXT,
                correoUsuario TEXT,
                lugar TEXT,
                descripcion TEXT,
                prioridad TEXT,
                fotoUri TEXT,
                estado TEXT,
                respuestaAdmin TEXT,
                solucion TEXT,
                fechaCreacionMs INTEGER,
                sincronizado INTEGER DEFAULT 1
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REPORTES")
        onCreate(db)
    }

    fun insertarOActualizarReporte(reporte: Reporte, sincronizado: Boolean = true) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", reporte.id)
            put("titulo", reporte.titulo)
            put("nombreUsuario", reporte.nombreUsuario)
            put("correoUsuario", reporte.correoUsuario)
            put("lugar", reporte.lugar)
            put("descripcion", reporte.descripcion)
            put("prioridad", reporte.prioridad)
            put("fotoUri", reporte.fotoUri)
            put("estado", reporte.estado)
            put("respuestaAdmin", reporte.respuestaAdmin)
            put("solucion", reporte.solucion)
            put("fechaCreacionMs", reporte.fechaCreacionMs)
            put("sincronizado", if (sincronizado) 1 else 0)
        }
        db.insertWithOnConflict(TABLE_REPORTES, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun obtenerTodosLosReportes(): List<Reporte> {
        val lista = mutableListOf<Reporte>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_REPORTES ORDER BY fechaCreacionMs DESC",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                val idxCorreo = it.getColumnIndex("correoUsuario")
                val correo = if (idxCorreo >= 0) it.getString(idxCorreo).orEmpty() else ""

                lista.add(
                    Reporte(
                        id = it.getString(it.getColumnIndexOrThrow("id")),
                        titulo = it.getString(it.getColumnIndexOrThrow("titulo")).orEmpty(),
                        nombreUsuario = it.getString(it.getColumnIndexOrThrow("nombreUsuario")).orEmpty(),
                        correoUsuario = correo,
                        lugar = it.getString(it.getColumnIndexOrThrow("lugar")).orEmpty(),
                        descripcion = it.getString(it.getColumnIndexOrThrow("descripcion")).orEmpty(),
                        prioridad = it.getString(it.getColumnIndexOrThrow("prioridad")).orEmpty(),
                        fotoUri = it.getString(it.getColumnIndexOrThrow("fotoUri")).orEmpty(),
                        estado = it.getString(it.getColumnIndexOrThrow("estado")).orEmpty(),
                        respuestaAdmin = it.getString(it.getColumnIndexOrThrow("respuestaAdmin")).orEmpty(),
                        solucion = it.getString(it.getColumnIndexOrThrow("solucion")).orEmpty(),
                        fechaCreacionMs = it.getLong(it.getColumnIndexOrThrow("fechaCreacionMs"))
                    )
                )
            }
        }
        return lista
    }

    fun obtenerPendientesDeSincronizar(): List<Reporte> {
        val lista = mutableListOf<Reporte>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_REPORTES WHERE sincronizado = 0",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                val idxCorreo = it.getColumnIndex("correoUsuario")
                val correo = if (idxCorreo >= 0) it.getString(idxCorreo).orEmpty() else ""

                lista.add(
                    Reporte(
                        id = it.getString(it.getColumnIndexOrThrow("id")),
                        titulo = it.getString(it.getColumnIndexOrThrow("titulo")).orEmpty(),
                        nombreUsuario = it.getString(it.getColumnIndexOrThrow("nombreUsuario")).orEmpty(),
                        correoUsuario = correo,
                        lugar = it.getString(it.getColumnIndexOrThrow("lugar")).orEmpty(),
                        descripcion = it.getString(it.getColumnIndexOrThrow("descripcion")).orEmpty(),
                        prioridad = it.getString(it.getColumnIndexOrThrow("prioridad")).orEmpty(),
                        fotoUri = it.getString(it.getColumnIndexOrThrow("fotoUri")).orEmpty(),
                        estado = it.getString(it.getColumnIndexOrThrow("estado")).orEmpty(),
                        respuestaAdmin = it.getString(it.getColumnIndexOrThrow("respuestaAdmin")).orEmpty(),
                        solucion = it.getString(it.getColumnIndexOrThrow("solucion")).orEmpty(),
                        fechaCreacionMs = it.getLong(it.getColumnIndexOrThrow("fechaCreacionMs"))
                    )
                )
            }
        }
        return lista
    }
}