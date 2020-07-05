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

class NoteFragmentViewModel : ViewModel() {
    private var _noteAllLiveData = MutableLiveData<List<Note>>()
    var noteAllLiveData: LiveData<List<Note>> = _noteAllLiveData
    private var compositeDisposable = CompositeDisposable()

    init {
        getAllNotes()
    }

    fun getAllNotes() {
        val allNoteList = NoteRepository().getAllNotes()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _noteAllLiveData.value = result
            }, { error -> Log.d("Error", error.message!!) })

        compositeDisposable.add(allNoteList)
    }

    fun deleteNote(note: Note) {
        NoteRepository().deleteNote(note)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}