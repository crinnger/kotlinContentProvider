package com.example.kotlincontentprovider.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

class NotesDataBaseHelper(context: Context?):SQLiteOpenHelper(context, NOME_BANCO, FACTORYDB, VERSAO_ATUAL) {
    companion object{
        private val NOME_BANCO= "notes.db"
        private val FACTORYDB: SQLiteDatabase.CursorFactory? = null
        private val VERSAO_ATUAL=1
        const val TABLE_NAME:String ="Notes"
        const val COLUMNS_TITULO = "titulo"
        const val COLUMNS_DESCRICAO = "descricao"
    }

    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "$_ID INTEGER PRIMARY KEY," +
            "$COLUMNS_TITULO TEXT not null," +
            "$COLUMNS_DESCRICAO TEXT not null" +
            ")"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion!=newVersion){
            db?.execSQL(DROP_TABLE)
            onCreate(db)
        }
    }

}