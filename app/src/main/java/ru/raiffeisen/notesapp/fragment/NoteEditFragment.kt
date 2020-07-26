package ru.raiffeisen.notesapp.fragment

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_note_update.*
import ru.raiffeisen.notesapp.R
import ru.raiffeisen.notesapp.db.helper.NoteDbHelper
import ru.raiffeisen.notesapp.model.Note
import ru.raiffeisen.notesapp.state.NoteEditFragmentState
import ru.raiffeisen.notesapp.view_model.NoteEditFragmentViewModel
import kotlin.properties.Delegates

class NoteEditFragment : BaseFragment<NoteEditFragmentViewModel>() {

    companion object {
        const val KEY = "Note"
    }

    private var db: SQLiteDatabase by Delegates.notNull()
    private var dbHelper: NoteDbHelper by Delegates.notNull()

    override fun createViewModel(): Lazy<NoteEditFragmentViewModel> = lazy {
        ViewModelProvider.NewInstanceFactory().create(NoteEditFragmentViewModel::class.java)
    }

    override var layoutId: Int = R.layout.fragment_note_update

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.arguments.let {
            viewModel.getNote(it?.getParcelable(KEY))
        }

        requireActivity().toolbarMainActivity.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        this.setMenuVisibility(false)
        setupDatabaseHelper()
        setupListeners()
    }

    override fun observeLiveData() {
        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NoteEditFragmentState.EditNoteState -> {
                    titleEditText.setText(it.note?.noteTitle)
                    bodyEditText.setText(it.note?.noteBody)
                }
                is NoteEditFragmentState.SuccessState -> {
                    this.setMenuVisibility(false)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_button_save_note, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save_note -> {
                val newNote = Note(
                    _id = null,
                    noteTitle = titleEditText.text.toString(),
                    noteBody = bodyEditText.text.toString()
                )
                viewModel.editNoteButtonClick(newNote)
            }
        }
        childFragmentManager.popBackStackImmediate()
        return super.onOptionsItemSelected(item)
    }

    private fun setupDatabaseHelper() {
        dbHelper = NoteDbHelper(requireContext())
        db = dbHelper.writableDatabase
    }

    private fun setupListeners() {
        requireActivity().toolbarMainActivity.setNavigationOnClickListener {
            childFragmentManager.popBackStackImmediate()
        }
    }
}