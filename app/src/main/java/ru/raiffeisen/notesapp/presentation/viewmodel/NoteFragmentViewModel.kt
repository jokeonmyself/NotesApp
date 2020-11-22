package ru.raiffeisen.notesapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.raiffeisen.notesapp.data.model.Note
import ru.raiffeisen.notesapp.data.repository.NoteRepository
import ru.raiffeisen.notesapp.presentation.state.NotesFragmentState

class NoteFragmentViewModel : BaseViewModel() {

    private val innerState: MutableLiveData<NotesFragmentState> = MutableLiveData(
        NotesFragmentState.NotesLoadedState(listOf())
    )
    val state: LiveData<NotesFragmentState> = innerState

    init {
        getAllNotes()
    }

    fun getAllNotes() {
        NoteRepository().getAllNotes()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                innerState.value = NotesFragmentState.NotesLoadedState(result)
            }, { error -> Log.d("Error", error.message!!) })
            .autoDispose()
    }

    fun deleteNote(note: Note) {
        NoteRepository().deleteNote(note)
    }
}