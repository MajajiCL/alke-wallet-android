package com.example.alkewallet.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "AlkeWallet.db"
        const val TABLE_USUARIO = "usuario"
        const val COLUMN_ID = "user_id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_EMAIL = "correo_electronico"
        const val COLUMN_PASSWORD = "contrasena"
        const val COLUMN_SALDO = "saldo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_USUARIO ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NOMBRE TEXT,"
                + "$COLUMN_EMAIL TEXT UNIQUE,"
                + "$COLUMN_PASSWORD TEXT,"
                + "$COLUMN_SALDO REAL" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIO")
        onCreate(db)
    }

    fun insertUser(nombre: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE, nombre)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, password)
        values.put(COLUMN_SALDO, 0.0)

        val result = db.insert(TABLE_USUARIO, null, values)
        db.close()
        return result != -1L
    }

    fun checkUser(email: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_EMAIL = ?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun loginUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?", arrayOf(email, password))
        val success = cursor.count > 0
        cursor.close()
        db.close()
        return success
    }
}
