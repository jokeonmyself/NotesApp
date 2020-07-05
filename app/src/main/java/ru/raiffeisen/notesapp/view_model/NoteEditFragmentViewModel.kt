package ru.raiffeisen.notesapp.view_model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.raiffeisen.di.ServiceLocator
import ru.raiffeisen.notesapp.helper.App
import ru.raiffeisen.notesapp.model.Note
import ru.raiffeisen.notesapp.state.NoteEditFragmentState
import java.io.File
import java.io.FileOutputStream
import java.util.*

class NoteEditFragmentViewModel : ViewModel() {

    private val _noteState: MutableLiveData<NoteEditFragmentState> =
        MutableLiveData(NoteEditFragmentState.CreateNoteState)
    val noteStateLiveData: LiveData<NoteEditFragmentState> = _noteState
    private var _linkLiveData = MutableLiveData<Uri>()
    var linkLiveData: LiveData<Uri> = _linkLiveData
    private var _requestImageCapture = MutableLiveData<Int>()
    var requestImageCapture: LiveData<Int> = _requestImageCapture
    private val noteRepository = ServiceLocator.noteRepository

    fun editNoteButtonClick(note: Note) = noteRepository.saveNote(note)

    fun imageSaveActivityResultStart(imgUri: Uri?) {
        if (imgUri != null) {
            _linkLiveData.value = imgUri
            val file = File(imgUri.toString())
            val inputStream = App.INSTANCE.contentResolver.openInputStream(imgUri)
            val outputStream: FileOutputStream =
                App.INSTANCE.openFileOutput(
                    "${file.name}${UUID.randomUUID()}.jpg",
                    Context.MODE_PRIVATE
                )
            val buffer = ByteArray(1024)
            inputStream.use {
                while (it!!.read(buffer) > 0)
                    outputStream.write(buffer)
            }
        }
    }

    fun fragmentInit(note: Note) {
        _noteState.value = NoteEditFragmentState.EditNoteState(note)
    }

    fun addImageButtonClick(requestImageCapture: Int) {
        _requestImageCapture.value = requestImageCapture
    }
}