package com.cyriltheandroid.noteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyriltheandroid.noteapp.model.NoteModel

/**
 * Cette classe gère les opérations de stockage et de récupération des tâches
 * Adapter pour afficher une liste de tâches dans un RecyclerView.
 * Les tâches sont stockées dans une liste de NoteModel et sont affichées à l'aide de item_note.
 * Le Listener permet de réagir à des actions comme la suppression ou la sélection d'une tâche.
 */

class NoteAdapter(var notes: List<NoteModel>, var listener: NoteListener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // Définition de l'interface NoteListener qui sera implémentée dans la MainActivity
    interface NoteListener {
        fun onItemClicked(position: Int)
        fun onDeleteNoteClicked(position: Int)
    }

    // Définition de la classe NoteViewHolder qui permet de récupérer les vues de l'élément de la liste
    class NoteViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val noteTitleTextView: TextView = view.findViewById(R.id.title_text_view)
        val noteDescTextView: TextView = view.findViewById(R.id.desc_text_view)
        val deleteNoteButton: ImageButton = view.findViewById(R.id.delete_button)
    }

    // Création d'une nouvelle vue pour l'élément de la liste
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val context = parent.context

        // Utilisation d'un LayoutInflater pour créer la vue à partir du layout item_note
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    // Remplissage de la vue avec les données de la note
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        // Mise à jour du titre et de la description de la note dans la vue
        holder.noteTitleTextView.text = note.title
        holder.noteDescTextView.text = note.desc

        // Définition des listeners pour le bouton de suppression et pour l'élément de la liste
        holder.deleteNoteButton.setOnClickListener {
            listener.onDeleteNoteClicked(position)
        }
        holder.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }
    }

    // Renvoi le nombre total d'éléments dans la liste
    override fun getItemCount(): Int = notes.size
}