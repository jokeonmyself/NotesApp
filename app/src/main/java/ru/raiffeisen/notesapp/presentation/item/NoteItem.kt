package ru.raiffeisen.notesapp.presentation.item

import androidx.recyclerview.widget.ItemTouchHelper
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.note_item.view.*
import ru.raiffeisen.notesapp.R
import ru.raiffeisen.notesapp.data.model.Note

class NoteItem(val note: Note): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textNoteTitle.text = note.noteTitle
    }

    override fun getLayout(): Int = R.layout.note_item

    override fun getSwipeDirs(): Int {
        return ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    }
}