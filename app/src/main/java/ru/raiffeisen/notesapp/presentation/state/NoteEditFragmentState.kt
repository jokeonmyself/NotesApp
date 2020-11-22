package ru.raiffeisen.notesapp.presentation.state

import ru.raiffeisen.notesapp.data.model.Note

sealed class NoteEditFragmentState {
    object CreateNoteState: NoteEditFragmentState()
    class EditNoteState(val note: Note?): NoteEditFragmentState()
    object SuccessState: NoteEditFragmentState()
}