package ru.raiffeisen.notesapp.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.raiffeisen.notesapp.model.Note
import ru.raiffeisen.notesapp.repository.NoteRepository
import ru.raiffeisen.notesapp.state.NotesFragmentState

class NoteFragmentViewModel : BaseViewModel() {

    private val innerState: MutableLiveData<NotesFragmentState> = MutableLiveData(
        NotesFragmentState.NotesLoadedState(listOf())
    )
    val state: LiveData<NotesFragmentState> = innerState

    private var _noteAllLiveData = MutableLiveData<List<Note>>()
    var noteAllLiveData: LiveData<List<Note>> = _noteAllLiveData

    init {
        getAllNotes()
    }

    fun getAllNotes() {
        val allNoteList = NoteRepository().getAllNotes()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _noteAllLiveData.value = result
                innerState.value = NotesFragmentState.NotesLoadedState(result)
            }, { error -> Log.d("Error", error.message!!) })
            .autoDispose()

    }

    fun deleteNote(note: Note) {
        NoteRepository().deleteNote(note)
    }
}