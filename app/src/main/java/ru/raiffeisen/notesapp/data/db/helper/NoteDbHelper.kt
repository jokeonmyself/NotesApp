package ru.raiffeisen.notesapp.data.db.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.raiffeisen.notesapp.data.db.NoteAppDatabaseSchema.NotesTable.Companion.COLUMN_BODY
import ru.raiffeisen.notesapp.data.db.NoteAppDatabaseSchema.NotesTable.Companion.COLUMN_ID
import ru.raiffeisen.notesapp.data.db.NoteAppDatabaseSchema.NotesTable.Companion.COLUMN_TITLE
import ru.raiffeisen.notesapp.data.db.NoteAppDatabaseSchema.NotesTable.Companion.TABLE_NAME

class NoteDbHelper(context: Context) : SQLiteOpenHelper(context, "noteapp.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE $TABLE_NAME" +
                "($COLUMN_ID  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_BODY TEXT;")
        db?.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = ("DROP TABLE IF EXISTS $TABLE_NAME")
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }
}