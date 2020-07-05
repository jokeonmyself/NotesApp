package ru.raiffeisen.notesapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    val _id: Int?,
    var noteTitle: String,
    var noteBody: String,
    var notePicLink: String?
) : Parcelable