package com.example.agroalerta.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        private const val DB_NAME = "agroaler.db"
        private const val DB_VERSION = 1
        const val TABLA_USUARIOS = "usuarios"
        const val COL_ID = "id"
        const val COL_NOMBRE = "nombre"
        const val COL_EMAIL = "email"
        const val COL_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLA_USUARIOS (
            $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_NOMBRE TEXT NOT NULL,
            $COL_EMAIL TEXT NOT NULL UNIQUE,
            $COL_PASSWORD TEXT NOT NULL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_USUARIOS")
        onCreate(db);
    }

    fun registrarUsuario(nombre: String, email: String, passwordHash: String): Boolean {
        val valores = ContentValues().apply {
            put(COL_NOMBRE, nombre)
            put(COL_EMAIL, email)
            put(COL_PASSWORD, passwordHash)
        }
        return writableDatabase.insert(TABLA_USUARIOS, null, valores) != 1L
    }

    fun validarLogin(email: String, passwordHash: String): Boolean {
        return readableDatabase.query(
            TABLA_USUARIOS,
            arrayOf(COL_ID),
            "$COL_EMAIL = ? AND $COL_PASSWORD = ?",
            arrayOf(email, passwordHash),
            null, null, null
        ).use { it.moveToFirst() }
    }
}