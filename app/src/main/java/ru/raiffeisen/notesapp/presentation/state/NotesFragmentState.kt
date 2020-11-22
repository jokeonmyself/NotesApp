package ru.raiffeisen.notesapp.presentation.state

import ru.raiffeisen.notesapp.data.model.Note

sealed class NotesFragmentState {
    class NotesLoadedState(val notes: List<Note>): NotesFragmentState()
}