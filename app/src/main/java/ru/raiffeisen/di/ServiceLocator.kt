package ru.raiffeisen.di

import ru.raiffeisen.notesapp.data.repository.NoteRepository

object ServiceLocator {
    val noteRepository by lazy {
        NoteRepository()
    }
}