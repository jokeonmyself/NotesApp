package ru.raiffeisen.notesapp.presentation.event

import ru.raiffeisen.notesapp.data.model.Note

sealed class NoteEditViewEvent {
    class ClickSaveNoteButton(val title: String, val body: String) : NoteEditViewEvent()
    class HaveNoteFromBundle(val note: Note?) : NoteEditViewEvent()
}