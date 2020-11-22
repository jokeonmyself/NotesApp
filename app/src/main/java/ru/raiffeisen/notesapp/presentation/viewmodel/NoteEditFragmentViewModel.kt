package ru.raiffeisen.notesapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.raiffeisen.di.ServiceLocator
import ru.raiffeisen.notesapp.data.model.Note
import ru.raiffeisen.notesapp.presentation.event.NoteEditViewEvent
import ru.raiffeisen.notesapp.presentation.event.NoteEditViewEvent.ClickSaveNoteButton
import ru.raiffeisen.notesapp.presentation.event.NoteEditViewEvent.HaveNoteFromBundle
import ru.raiffeisen.notesapp.presentation.state.NoteEditFragmentState
import ru.raiffeisen.notesapp.presentation.state.NoteEditFragmentState.EditNoteState
import ru.raiffeisen.notesapp.presentation.state.NoteEditFragmentState.SuccessState

class NoteEditFragmentViewModel : ViewModel() {

    fun state(): MutableLiveData<NoteEditFragmentState> =
        MutableLiveData(NoteEditFragmentState.CreateNoteState)

    private val noteRepository = ServiceLocator.noteRepository

    private var idNote: Int? = null

    fun perform(viewEvent: NoteEditViewEvent) {
        when (viewEvent) {
            is HaveNoteFromBundle -> {
                getNote(viewEvent.note)
            }
            is ClickSaveNoteButton -> {
                editNoteButtonClick(
                    Note(idNote, viewEvent.title, viewEvent.body)
                )
            }
        }
    }

    private fun editNoteButtonClick(note: Note) {
        noteRepository.saveNote(note)
        state().value = SuccessState
    }

    private fun getNote(note: Note?) {
        idNote = note?._id
        state().value = EditNoteState(note)
    }
}