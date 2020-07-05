package ru.raiffeisen.notesapp.fragment

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_note_update.*
import ru.raiffeisen.notesapp.R
import ru.raiffeisen.notesapp.db.helper.NoteDbHelper
import ru.raiffeisen.notesapp.model.Note
import ru.raiffeisen.notesapp.state.NoteEditFragmentState
import ru.raiffeisen.notesapp.view_model.NoteEditFragmentViewModel
import kotlin.properties.Delegates

class NoteEditFragment : Fragment() {

    private var note = Note(_id = null, noteTitle = "", noteBody = "", notePicLink = "")
    private val viewModel by createViewModel()
    private var db: SQLiteDatabase by Delegates.notNull()
    private var dbHelper: NoteDbHelper by Delegates.notNull()

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    private fun createViewModel() =
        lazy { ViewModelProviders.of(this).get(NoteEditFragmentViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        observeLiveData()
        dbHelper = NoteDbHelper(requireContext())
        db = dbHelper.writableDatabase
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_note_update, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_button_save_note, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save_note -> {
                note.noteTitle = titleEditText.text.toString()
                note.noteBody = bodyEditText.text.toString()
                viewModel.editNoteButtonClick(note)
            }
        }
        fragmentManager?.popBackStackImmediate()
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            val bundleNote: Note? = bundle.getParcelable("Note")
            if (bundleNote != null)
                note = bundleNote
        }
        viewModel.fragmentInit(note)

        requireActivity().toolbarMainActivity.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        requireActivity().toolbarMainActivity.setNavigationOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }

        addResButton.setOnClickListener {
            note.noteTitle = titleEditText.text.toString()
            note.noteBody = bodyEditText.text.toString()
            viewModel.editNoteButtonClick(note)
        }
        addResButton.setOnClickListener {
            viewModel.addImageButtonClick(REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun observeLiveData() {
        viewModel.noteStateLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NoteEditFragmentState.EditNoteState -> {
                    titleEditText.setText(it.note.noteTitle)
                    bodyEditText.setText(it.note.noteBody)
                    linkTextView.isVisible = true
                    linkTextView.visibility = View.VISIBLE
                    linkTextView.text = it.note.notePicLink
                    addResButton.isVisible = false
                }
            }
        })
        viewModel.requestImageCapture.observe(viewLifecycleOwner, Observer {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select picture"), it)
        })
        viewModel.linkLiveData.observe(viewLifecycleOwner, Observer {
            note.notePicLink = it.path
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            viewModel.imageSaveActivityResultStart(data?.data)
        }
    }
}