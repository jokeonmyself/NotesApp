package ru.raiffeisen.notesapp.data.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import io.reactivex.Single
import ru.raiffeisen.notesapp.data.db.NoteAppDatabaseSchema.NotesTable.Companion.COLUMN_BODY
import ru.raiffeisen.notesapp.data.db.NoteAppDatabaseSchema.NotesTable.Companion.COLUMN_ID
import ru.raiffeisen.notesapp.data.db.NoteAppDatabaseSchema.NotesTable.Companion.COLUMN_TITLE
import ru.raiffeisen.notesapp.data.db.NoteAppDatabaseSchema.NotesTable.Companion.TABLE_NAME
import ru.raiffeisen.notesapp.data.db.helper.NoteDbHelper
import ru.raiffeisen.notesapp.data.helper.App
import ru.raiffeisen.notesapp.data.model.Note
import kotlin.properties.Delegates

class NoteRepository {
    var db: SQLiteDatabase by Delegates.notNull()
    var dbHelper: NoteDbHelper by Delegates.notNull()
    var allNotesList = arrayListOf<Note>()

    init {
        dbHelper = NoteDbHelper(App.INSTANCE)
        db = dbHelper.writableDatabase
    }

    fun getAllNotes(): Single<List<Note>> {
        val allNotesCursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        allNotesCursor.moveToPosition(-1)
        return Single.create { emitter ->
            allNotesCursor.use { cursor ->
                while (cursor.moveToNext()) {
                    allNotesList.add(
                        Note(
                            cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)) ?: "",
                            cursor.getString(cursor.getColumnIndex(COLUMN_BODY)) ?: "")
                    )
                }
                emitter.onSuccess(allNotesList)
            }
        }
    }

    fun saveNote(note: Note) {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, note.noteTitle)
        contentValues.put(COLUMN_BODY, note.noteBody)
        if (note._id != null) {
            db.update(TABLE_NAME, contentValues, "_id = ?", arrayOf(note._id.toString()))
        } else {
            db.insert(TABLE_NAME, null, contentValues)
        }
    }

    fun deleteNote(note: Note) {
        if (note._id != null) {
            db.delete(TABLE_NAME, "_id = ?", arrayOf(note._id.toString()))
        }
    }
}