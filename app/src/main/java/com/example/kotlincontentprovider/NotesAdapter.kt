package com.example.kotlincontentprovider

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincontentprovider.database.NotesDataBaseHelper.Companion.COLUMNS_DESCRICAO
import com.example.kotlincontentprovider.database.NotesDataBaseHelper.Companion.COLUMNS_TITULO

class NotesAdapter(private val noteClickListener: NoteClickListener): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var mCursor:Cursor? = null

   class NotesViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
       val noteTitulo = itemView.findViewById<TextView>(R.id.note_item_titulo)
       val noteDescricao = itemView.findViewById<TextView>(R.id.note_item_descricao)
       val noteButtomRemove = itemView.findViewById(R.id.note_item_button_excluir) as Button

   }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent,false))
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        mCursor?.moveToPosition(position)
        holder.noteTitulo.text=mCursor?.getString(mCursor?.getColumnIndex(COLUMNS_TITULO) as Int)
        holder.noteDescricao.text=mCursor?.getString(mCursor?.getColumnIndex(COLUMNS_DESCRICAO) as Int)
        holder.noteButtomRemove.setOnClickListener{
            mCursor?.moveToPosition(position)
            noteClickListener.noteRemoveItem(mCursor)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener{
            noteClickListener.noteClickItem(mCursor as Cursor)
        }
    }

    override fun getItemCount(): Int = if(mCursor!=null) mCursor?.count as Int else 0

    fun setCursor(newCursor: Cursor?){
        mCursor=newCursor
        notifyDataSetChanged()
    }
}