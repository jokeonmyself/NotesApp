package ru.raiffeisen.notesapp.state

import ru.raiffeisen.notesapp.model.Note

sealed class NoteEditFragmentState {
    object CreateNoteState: NoteEditFragmentState()
    class EditNoteState(val note: Note): NoteEditFragmentState()
}