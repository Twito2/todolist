package com.cyriltheandroid.noteapp

// CreateNoteActivity.kt contient la logique pour créer une nouvelle tâche

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class CreateNoteActivity : AppCompatActivity() {

    // Déclarer les vues nécessaires
    lateinit var noteTitleEditText: EditText
    lateinit var noteDescEditText: EditText
    lateinit var createNoteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        // Initialiser les vues
        noteTitleEditText = findViewById(R.id.note_title_edit_text)
        noteDescEditText = findViewById(R.id.note_desc_edit_text)
        createNoteButton = findViewById(R.id.create_note_button)

        // Définir le click listener pour le bouton de création de tâche
        createNoteButton.setOnClickListener {

            // Récupérer le titre et la description de la tâche à partir des champs d'édition
            val title = noteTitleEditText.text.toString()
            val desc = noteDescEditText.text.toString()

            // Créer un intent pour renvoyer les informations de la tâche à MainActivity
            val intent = Intent()
            intent.putExtra(NOTE_TITLE, title)
            intent.putExtra(NOTE_DESC, desc)

            // Définir le résultat de l'activité en tant que RESULT_OK et inclure l'intent
            setResult(Activity.RESULT_OK, intent)

            // Fermer l'activité
            finish()
        }
    }
}