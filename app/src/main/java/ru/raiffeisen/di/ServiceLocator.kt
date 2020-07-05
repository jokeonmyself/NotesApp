package ru.raiffeisen.di

import ru.raiffeisen.notesapp.repository.NoteRepository

object ServiceLocator {
    val noteRepository by lazy {
        NoteRepository()
    }
}