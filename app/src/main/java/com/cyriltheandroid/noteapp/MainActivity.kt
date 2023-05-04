package com.cyriltheandroid.noteapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyriltheandroid.noteapp.model.NoteModel
import com.cyriltheandroid.noteapp.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NoteAdapter.NoteListener {

    lateinit var addNewNoteButton: FloatingActionButton
    lateinit var noteRecyclerView: RecyclerView
    var notes = mutableListOf<NoteModel>()
    lateinit var noteViewModel: NoteViewModel

    private val createNoteActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val title = data?.getStringExtra(NOTE_TITLE)
            val desc = data?.getStringExtra(NOTE_DESC)

            val noteModel = NoteModel(title!!, desc!!)
            notes.add(0, noteModel)
            noteRecyclerView.adapter?.notifyItemChanged(0)
            noteViewModel.saveNotes(notes)
        }
    }

    private val noteDetailsActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val title = data?.getStringExtra(NOTE_TITLE)
            val desc = data?.getStringExtra(NOTE_DESC)
            val position = data?.getIntExtra(NOTE_POSITION, -1)

            // Mise à jour du modèle de note à la position donnée
            if (position != -1) {
                notes[position!!].title = title!!
                notes[position].desc = desc!!
                noteRecyclerView.adapter?.notifyItemChanged(position)

                // Sauvegarde de la liste des notes dans les SharedPreferences via le ViewModel
                noteViewModel.saveNotes(notes)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation du ViewModel et configuration des SharedPreferences
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        noteViewModel.setSharedPreferences(this)

        // Observateur du LiveData des notes
        noteViewModel.notesLiveData.observe(this) {
            // Ajout des notes récupérées depuis la base de données
            notes.addAll(it)
            // Initialisation de la RecyclerView
            initNoteRecyclerView()
        }

        // Récupération du bouton de création de nouvelle note et configuration du click listener
        addNewNoteButton = findViewById(R.id.add_new_note_button)
        addNewNoteButton.setOnClickListener {
            val intent = Intent(this, CreateNoteActivity::class.java)
            createNoteActivityResult.launch(intent)
        }
    }

    // Initialisation du RecyclerView et configuration de l'adapter et du layout manager
    private fun initNoteRecyclerView() {
        noteRecyclerView = findViewById(R.id.note_recycler_view)
        val adapter = NoteAdapter(notes, this)
        val layoutManager = LinearLayoutManager(this)

        noteRecyclerView.adapter = adapter
        noteRecyclerView.layoutManager = layoutManager
    }

    private fun showDeleteNoteAlertDialog(note: NoteModel, position: Int) {

        // Création d'une boîte de dialogue pour demander la confirmation de suppression de la note
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Suppression de la note ${note.title}")
            .setMessage("Êtes-vous certain de vouloir supprimer la note ?")
            .setIcon(android.R.drawable.ic_menu_delete)

            // Si l'utilisateur confirme la suppression, on appelle la fonction deleteNote et on affiche un toast de confirmation
            .setPositiveButton("Supprimer") { dialog, _ ->
                dialog.dismiss()
                deleteNote(position)
                displayToast("La note ${note.title} a bien été supprimée.")
            }
            // Si l'utilisateur annule la suppression, on ne fait rien
            .setNegativeButton("Annuler", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteNote(position: Int) {
        // Suppression de la note à la position donnée
        notes.removeAt(position)

        // Mise à jour de l'affichage de la liste de notes
        noteRecyclerView.adapter?.notifyItemRemoved(position)

        // Sauvegarde des notes dans le ViewModel
        noteViewModel.saveNotes(notes)
    }

    private fun displayToast(message: String) {

        // Affichage d'un toast avec le message donné
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemClicked(position: Int) {

        // Lorsqu'un élément de la liste de notes est cliqué, on ouvre l'activité de détails de note correspondante
        val note = notes[position]
        val intent = Intent(this, NoteDetailsActivity::class.java)
        intent.putExtra(NOTE_TITLE, note.title)
        intent.putExtra(NOTE_DESC, note.desc)
        intent.putExtra(NOTE_POSITION, position)
        noteDetailsActivityResult.launch(intent)
    }

    override fun onDeleteNoteClicked(position: Int) {

        // Lorsque le bouton de suppression d'une note est cliqué, on affiche la boîte de dialogue de confirmation de suppression
        showDeleteNoteAlertDialog(notes[position], position)
    }
}