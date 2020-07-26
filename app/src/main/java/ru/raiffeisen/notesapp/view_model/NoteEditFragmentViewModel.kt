package ru.raiffeisen.notesapp.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.raiffeisen.di.ServiceLocator
import ru.raiffeisen.notesapp.model.Note
import ru.raiffeisen.notesapp.state.NoteEditFragmentState
import ru.raiffeisen.notesapp.state.NoteEditFragmentState.SuccessState

class NoteEditFragmentViewModel : ViewModel() {

    private val innerState: MutableLiveData<NoteEditFragmentState> =
        MutableLiveData(NoteEditFragmentState.CreateNoteState)
    val state: LiveData<NoteEditFragmentState> = innerState

    private val noteRepository = ServiceLocator.noteRepository

    fun editNoteButtonClick(note: Note) {
        noteRepository.saveNote(note)
        innerState.value = SuccessState
    }

    fun getNote(note: Note?) {
        innerState.value = NoteEditFragmentState.EditNoteState(note)
    }
}