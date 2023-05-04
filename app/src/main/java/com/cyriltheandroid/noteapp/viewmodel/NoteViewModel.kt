package com.cyriltheandroid.noteapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.cyriltheandroid.noteapp.NOTES_CONFIG
import com.cyriltheandroid.noteapp.NoteRepository
import com.cyriltheandroid.noteapp.model.NoteModel

class NoteViewModel: ViewModel() {

    private lateinit var noteRepository: NoteRepository
    lateinit var notesLiveData: LiveData<List<NoteModel>>

    // Fonction pour initialiser le Repository et obtenir les notes sauvegardées
    fun setSharedPreferences(context: Context) {

        // Obtention des SharedPreferences de l'application
        val sharedPreferences = context.getSharedPreferences(NOTES_CONFIG, Context.MODE_PRIVATE)

        // Initialisation du Repository en utilisant les SharedPreferences
        noteRepository = NoteRepository(sharedPreferences)

        // Obtention de la liste des notes sauvegardées à partir du Repository et la conversion en LiveData
        notesLiveData = noteRepository.getSavedNotes().asLiveData()
    }

    // Fonction pour sauvegarder la liste des notes
    fun saveNotes(notesToSave: List<NoteModel>) {
        // Appelle la fonction saveNotes du Repository pour sauvegarder la liste des notes
        noteRepository.saveNotes(notesToSave)
    }
}