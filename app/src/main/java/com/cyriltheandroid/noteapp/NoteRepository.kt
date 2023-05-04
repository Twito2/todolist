package com.cyriltheandroid.noteapp

import android.content.SharedPreferences
import com.cyriltheandroid.noteapp.model.NoteModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//la classe NoteRepository permet de sauvegarder les tâches et d'afficher la liste des notes sauvegardées.

class NoteRepository(private val sharedPreferences: SharedPreferences) {

    // Initialisation de l'objet Gson
    private val gson = Gson()

    // Fonction permettant de récupérer la liste des notes sauvegardées
    // Utilisation de la coroutine flow pour renvoyer un flux de données asynchrones
    fun getSavedNotes() : Flow<List<NoteModel>> = flow {

        // Récupération des notes sauvegardées au format JSON depuis les SharedPreferences
        val serializedNotes = sharedPreferences.getString(SAVED_NOTES, null)

        // Définition d'un type de données pour désérialiser les notes JSON en List<NoteModel>
        val typeToken = object: TypeToken<List<NoteModel>>(){}.type

        // Vérification si des notes ont été sauvegardées précédemment
        if (serializedNotes != null) {

            // Émission des notes sauvegardées dans le flux de données
            emit(gson.fromJson(serializedNotes, typeToken))
        } else {

            // Émission d'une liste vide si aucune note n'a été sauvegardée précédemment
            emit(ArrayList<NoteModel>())
        }
    }

    // Fonction permettant de sauvegarder la liste des notes
    fun saveNotes(notesToSave: List<NoteModel>) {

        // Sérialisation de la liste des notes en JSON
        val serializedNotesToSave = gson.toJson(notesToSave)

        // Enregistrement de la liste des notes sérialisée dans les SharedPreferences
        with(sharedPreferences.edit()) {
            putString(SAVED_NOTES, serializedNotesToSave)
            apply()
        }
    }
}