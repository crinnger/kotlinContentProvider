package com.example.kotlincontentprovider.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.UnsupportedSchemeException
import android.net.Uri
import android.provider.BaseColumns._ID
import android.util.Log

class NoteProvider : ContentProvider() {

    private lateinit var mUriMatcher: UriMatcher
    private lateinit var dbHelper:NotesDataBaseHelper

    companion object{
        const val AUTHORITY = "com.example.kotlincontentprovider.provider"
        val BASE_URI = Uri.parse("content://$AUTHORITY")
        val URI_NOTES = Uri.withAppendedPath(BASE_URI,"notes")
        //"content://com.example.kotlincontentprovider.provider/notes"
        const val NOTES=1
        const val NOTES_BY_ID=2
    }

    override fun onCreate(): Boolean {
        mUriMatcher= UriMatcher(UriMatcher.NO_MATCH)
        mUriMatcher.addURI(AUTHORITY,"notes", NOTES)
        mUriMatcher.addURI(AUTHORITY,"notes/#", NOTES_BY_ID)
        if(context!=null){
            dbHelper= NotesDataBaseHelper(context as Context)
        }
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if(mUriMatcher.match(uri)== NOTES_BY_ID ){
            val db = dbHelper.writableDatabase
            val linhasAfetadas= db.delete(NotesDataBaseHelper.TABLE_NAME,"$_ID = ?", arrayOf((uri.lastPathSegment)))
            db.close()
            context?.contentResolver?.notifyChange(uri,null)
            return linhasAfetadas
        }else{
            throw UnsupportedSchemeException("URI invalida para exclusao")
        }
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedSchemeException("URI nao implementada")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if(mUriMatcher.match(uri)== NOTES ){
            val db = dbHelper.writableDatabase
            val id= db.insert(NotesDataBaseHelper.TABLE_NAME,null, values)
            val insertUri= Uri.withAppendedPath(BASE_URI,id.toString())
            db.close()
            context?.contentResolver?.notifyChange(uri,null)
            return insertUri
        }else{
            throw UnsupportedSchemeException("URI invalida para insersao")
        }
    }

     override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
         Log.e("debug","URI : ${uri.toString()}")
         return when{
             dbHelper==null ->{
                 throw UnsupportedSchemeException("dbHelper nao inicializado")
                 null
             }
             mUriMatcher.match(uri) == NOTES -> {
                 val db:SQLiteDatabase = dbHelper.readableDatabase
                 val cursor:Cursor=db.query(NotesDataBaseHelper.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder)
                 cursor.setNotificationUri(context?.contentResolver,uri)
                 cursor
             }
             mUriMatcher.match(uri) == NOTES_BY_ID -> {
                 val db:SQLiteDatabase = dbHelper.readableDatabase
                 val cursor:Cursor=db.query(NotesDataBaseHelper.TABLE_NAME,projection,"$_ID = ?",
                     arrayOf(uri.lastPathSegment),null,null,sortOrder)
                 cursor.setNotificationUri(context?.contentResolver,uri)
                 cursor
             }
             else -> {
                 throw UnsupportedSchemeException("URI nao implementada")
                 null
             }
         }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if(mUriMatcher.match(uri) == NOTES_BY_ID) {
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val linhasAfetadas= db.update(NotesDataBaseHelper.TABLE_NAME,values,"$_ID = ?", arrayOf(uri.lastPathSegment))
            db.close()
            context?.contentResolver?.notifyChange(uri,null)
            return linhasAfetadas
        } else {
            throw UnsupportedSchemeException("URI nao implementada")
        }
    }
}