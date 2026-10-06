package com.example.alkewallet.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.alkewallet.data.model.TransactionItem
import com.example.alkewallet.data.model.UserProfile

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "AlkeWallet.db"

        // Tabla Usuarios
        const val TABLE_USUARIO = "usuario"
        const val COLUMN_ID = "user_id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_EMAIL = "correo_electronico"
        const val COLUMN_PASSWORD = "contrasena"
        const val COLUMN_SALDO = "saldo"
        const val COLUMN_PHOTO_URL = "foto_url"
        const val COLUMN_TOKEN = "token"

        // Tabla Transacciones (Persistencia Local para Modo Offline)
        const val TABLE_TRANSACCIONES = "transacciones"
        const val COLUMN_TX_ID = "tx_id"
        const val COLUMN_TX_TITLE = "title"
        const val COLUMN_TX_DATE = "date"
        const val COLUMN_TX_AMOUNT = "amount"
        const val COLUMN_TX_TYPE = "type" // DEPOSIT o EXPENSE
        const val COLUMN_TX_USER_EMAIL = "user_email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableUsuario = ("CREATE TABLE $TABLE_USUARIO ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NOMBRE TEXT,"
                + "$COLUMN_EMAIL TEXT UNIQUE,"
                + "$COLUMN_PASSWORD TEXT,"
                + "$COLUMN_SALDO REAL,"
                + "$COLUMN_PHOTO_URL TEXT,"
                + "$COLUMN_TOKEN TEXT" + ")")
        db.execSQL(createTableUsuario)

        val createTableTx = ("CREATE TABLE $TABLE_TRANSACCIONES ("
                + "$COLUMN_TX_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_TX_TITLE TEXT,"
                + "$COLUMN_TX_DATE TEXT,"
                + "$COLUMN_TX_AMOUNT REAL,"
                + "$COLUMN_TX_TYPE TEXT,"
                + "$COLUMN_TX_USER_EMAIL TEXT" + ")")
        db.execSQL(createTableTx)

        // Insertar usuario demo Amanda y transacciones iniciales
        seedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACCIONES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIO")
        onCreate(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        // Usuario demo
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, "Amanda Alkemy")
            put(COLUMN_EMAIL, "amanda@alkewallet.com")
            put(COLUMN_PASSWORD, "1234")
            put(COLUMN_SALDO, 124.57)
            put(COLUMN_PHOTO_URL, "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400")
            put(COLUMN_TOKEN, "demo_jwt_token_amanda_123456")
        }
        db.insert(TABLE_USUARIO, null, values)

        // Transacciones del diseño de Figma
        val txs = listOf(
            Triple("Yara Khalil", 15.00, "EXPENSE"),
            Triple("Sara Ibrahim", 20.50, "DEPOSIT"),
            Triple("Ahmad Ibrahim", 12.40, "DEPOSIT"),
            Triple("Reem Khaled", 21.30, "EXPENSE"),
            Triple("Hiba Saleh", 9.00, "DEPOSIT")
        )

        for ((title, amount, type) in txs) {
            val txVal = ContentValues().apply {
                put(COLUMN_TX_TITLE, title)
                put(COLUMN_TX_DATE, "Oct 14, 10:24 AM")
                put(COLUMN_TX_AMOUNT, amount)
                put(COLUMN_TX_TYPE, type)
                put(COLUMN_TX_USER_EMAIL, "amanda@alkewallet.com")
            }
            db.insert(TABLE_TRANSACCIONES, null, txVal)
        }
    }

    // --- Métodos de Usuarios ---
    fun insertUser(nombre: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_SALDO, 100.0) // Bono inicial de bienvenida
            put(COLUMN_PHOTO_URL, "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400")
            put(COLUMN_TOKEN, "jwt_" + System.currentTimeMillis())
        }
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
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email, password)
        )
        val success = cursor.count > 0
        cursor.close()
        db.close()
        return success
    }

    fun getUser(email: String): UserProfile? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_EMAIL = ?", arrayOf(email))
        var user: UserProfile? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
            val mail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val saldo = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SALDO))
            val photo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_URL))
            val token = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOKEN))
            user = UserProfile(id, name, mail, saldo, photo, token)
        }
        cursor.close()
        db.close()
        return user
    }

    fun updateBalance(email: String, newBalance: Double): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SALDO, newBalance)
        }
        val rows = db.update(TABLE_USUARIO, values, "$COLUMN_EMAIL = ?", arrayOf(email))
        db.close()
        return rows > 0
    }

    // --- Métodos de Transacciones (Room/CRUD) ---
    fun insertTransaction(title: String, date: String, amount: Double, type: String, email: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TX_TITLE, title)
            put(COLUMN_TX_DATE, date)
            put(COLUMN_TX_AMOUNT, amount)
            put(COLUMN_TX_TYPE, type)
            put(COLUMN_TX_USER_EMAIL, email)
        }
        val res = db.insert(TABLE_TRANSACCIONES, null, values)
        db.close()
        return res != -1L
    }

    fun getTransactions(email: String): List<TransactionItem> {
        val list = mutableListOf<TransactionItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_TRANSACCIONES WHERE $COLUMN_TX_USER_EMAIL = ? ORDER BY $COLUMN_TX_ID DESC",
            arrayOf(email)
        )
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TX_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TX_TITLE))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TX_DATE))
                val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TX_AMOUNT))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TX_TYPE))
                list.add(TransactionItem(id, title, date, amount, type))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }
}
