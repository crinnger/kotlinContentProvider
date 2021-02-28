package com.example.kotlincontentprovider

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.kotlincontentprovider.database.NoteProvider.Companion.URI_NOTES
import com.example.kotlincontentprovider.database.NotesDataBaseHelper.Companion.COLUMNS_DESCRICAO
import com.example.kotlincontentprovider.database.NotesDataBaseHelper.Companion.COLUMNS_TITULO

class NotesDetailFragment:DialogFragment(), DialogInterface.OnClickListener {
    private lateinit var noteEditTitulo:EditText
    private lateinit var noteEditDescricao:EditText
    private var id:Long=0

    companion object{
        private const val EXTRA_ID="id"
        fun newInstance(id:Long):NotesDetailFragment{
            val bundle=Bundle()
            bundle.putLong(EXTRA_ID,id)

            val notesDetailFragment=NotesDetailFragment()
            notesDetailFragment.arguments=bundle
            return notesDetailFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.note_detail,null)
        noteEditTitulo = view?.findViewById(R.id.note_detail_titulo) as EditText
        noteEditDescricao = view?.findViewById(R.id.note_detail_descricao) as EditText

        var newNote = true
        if(arguments!=null && arguments?.getLong(EXTRA_ID)!=0L){
            id=arguments?.getLong(EXTRA_ID) as Long
            val uri:Uri = Uri.withAppendedPath(URI_NOTES,id.toString())
            val cursor: Cursor?=activity?.contentResolver?.query(uri,null,null,null,null)
            if(cursor?.moveToNext() as Boolean){
                newNote=false
                noteEditTitulo.setText(cursor.getString(cursor.getColumnIndex(COLUMNS_TITULO)))
                noteEditDescricao.setText(cursor.getString(cursor.getColumnIndex(COLUMNS_DESCRICAO)))
            }
            cursor.close()
        }
        return AlertDialog.Builder(activity as Activity)
                .setTitle(if(newNote)"Nova Mensagem" else "Editar Mensagem")
                .setView(view)
                .setPositiveButton("Salvar",this)
                .setNegativeButton("Cancelar",this)
                .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val values = ContentValues()
        values.put(COLUMNS_TITULO,noteEditTitulo.text.toString())
        values.put(COLUMNS_DESCRICAO,noteEditDescricao.text.toString())
        if(id!=0L){
            val uri:Uri = Uri.withAppendedPath(URI_NOTES,id.toString())
            context?.contentResolver?.update(uri,values,null,null)
        }else{
            context?.contentResolver?.insert(URI_NOTES,values)
        }
    }
}