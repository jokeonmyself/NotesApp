package ru.raiffeisen.notesapp.db

class NoteAppDatabaseSchema {
    class NotesTable {
        companion object {
            const val TABLE_NAME = "notes"
            const val COLUMN_ID = "_id"
            const val COLUMN_TITLE = "title"
            const val COLUMN_BODY = "body"
            const val COLUMN_LINK = "link"
        }
    }
}