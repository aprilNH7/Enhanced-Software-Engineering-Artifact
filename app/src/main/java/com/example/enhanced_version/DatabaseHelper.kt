package com.example.enhanced_version

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "InventoryApp.db"
        const val DATABASE_VERSION = 1

        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"

        const val TABLE_ITEMS = "items"
        const val COLUMN_ITEM_ID = "id"
        const val COLUMN_ITEM_NAME = "name"
        const val COLUMN_ITEM_QUANTITY = "quantity"
        const val COLUMN_ITEM_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()

        val createItemsTable = """
            CREATE TABLE $TABLE_ITEMS (
                $COLUMN_ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ITEM_NAME TEXT NOT NULL,
                $COLUMN_ITEM_QUANTITY INTEGER NOT NULL,
                $COLUMN_ITEM_PRICE REAL NOT NULL
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createItemsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ITEMS")
        onCreate(db)
    }

    fun registerUser(username: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val result = db.insert(TABLE_USERS, null, values)
        return result != -1L
    }

    fun validateUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }

    fun getAllProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ITEMS", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_NAME))
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_QUANTITY))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ITEM_PRICE))
                productList.add(Product(id, name, quantity, price))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productList
    }

    fun addItem(name: String, quantity: Int, price: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ITEM_NAME, name)
            put(COLUMN_ITEM_QUANTITY, quantity)
            put(COLUMN_ITEM_PRICE, price)
        }
        val result = db.insert(TABLE_ITEMS, null, values)
        return result != -1L
    }
}
