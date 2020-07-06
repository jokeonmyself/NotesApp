package ru.raiffeisen.notesapp.state

import ru.raiffeisen.notesapp.model.Note

sealed class NotesFragmentState {
    class NotesLoadedState(val notes: List<Note>): NotesFragmentState()
}