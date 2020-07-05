package ru.raiffeisen.notesapp.helper

import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.TouchCallback

internal abstract class SwipeTouchCallback : TouchCallback() {


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }
}