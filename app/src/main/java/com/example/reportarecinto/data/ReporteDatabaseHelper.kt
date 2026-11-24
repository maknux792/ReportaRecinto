package com.example.reportarecinto.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.reportarecinto.model.Reporte

class ReporteDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "reportarecinto.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_REPORTES = "reportes"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_REPORTES (
                id TEXT PRIMARY KEY,
                titulo TEXT,
                nombreUsuario TEXT,
                lugar TEXT,
                descripcion TEXT,
                prioridad TEXT,
                fotoUri TEXT,
                estado TEXT,
                respuestaAdmin TEXT,
                solucion TEXT,
                fechaCreacionMs INTEGER,
                sincronizado INTEGER DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REPORTES")
        onCreate(db)
    }

    fun insertarOActualizarReporte(reporte: Reporte, sincronizado: Boolean = false) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", reporte.id)
            put("titulo", reporte.titulo)
            put("nombreUsuario", reporte.nombreUsuario)
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
        val cursor = db.rawQuery("SELECT * FROM $TABLE_REPORTES ORDER BY fechaCreacionMs DESC", null)
        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Reporte(
                        id = it.getString(it.getColumnIndexOrThrow("id")),
                        titulo = it.getString(it.getColumnIndexOrThrow("titulo")),
                        nombreUsuario = it.getString(it.getColumnIndexOrThrow("nombreUsuario")),
                        lugar = it.getString(it.getColumnIndexOrThrow("lugar")),
                        descripcion = it.getString(it.getColumnIndexOrThrow("descripcion")),
                        prioridad = it.getString(it.getColumnIndexOrThrow("prioridad")),
                        fotoUri = it.getString(it.getColumnIndexOrThrow("fotoUri")),
                        estado = it.getString(it.getColumnIndexOrThrow("estado")),
                        respuestaAdmin = it.getString(it.getColumnIndexOrThrow("respuestaAdmin")),
                        solucion = it.getString(it.getColumnIndexOrThrow("solucion")),
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
        val cursor = db.rawQuery("SELECT * FROM $TABLE_REPORTES WHERE sincronizado = 0", null)
        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Reporte(
                        id = it.getString(it.getColumnIndexOrThrow("id")),
                        titulo = it.getString(it.getColumnIndexOrThrow("titulo")),
                        nombreUsuario = it.getString(it.getColumnIndexOrThrow("nombreUsuario")),
                        lugar = it.getString(it.getColumnIndexOrThrow("lugar")),
                        descripcion = it.getString(it.getColumnIndexOrThrow("descripcion")),
                        prioridad = it.getString(it.getColumnIndexOrThrow("prioridad")),
                        fotoUri = it.getString(it.getColumnIndexOrThrow("fotoUri")),
                        estado = it.getString(it.getColumnIndexOrThrow("estado")),
                        respuestaAdmin = it.getString(it.getColumnIndexOrThrow("respuestaAdmin")),
                        solucion = it.getString(it.getColumnIndexOrThrow("solucion")),
                        fechaCreacionMs = it.getLong(it.getColumnIndexOrThrow("fechaCreacionMs"))
                    )
                )
            }
        }
        return lista
    }

    fun eliminarTodos() {
        writableDatabase.execSQL("DELETE FROM $TABLE_REPORTES")
    }
}
