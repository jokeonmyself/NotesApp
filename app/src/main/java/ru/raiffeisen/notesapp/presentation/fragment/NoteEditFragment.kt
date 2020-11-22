package ru.raiffeisen.notesapp.presentation.fragment

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_note_update.*
import ru.raiffeisen.notesapp.R
import ru.raiffeisen.notesapp.presentation.event.NoteEditViewEvent.ClickSaveNoteButton
import ru.raiffeisen.notesapp.presentation.event.NoteEditViewEvent.HaveNoteFromBundle
import ru.raiffeisen.notesapp.presentation.state.NoteEditFragmentState.*
import ru.raiffeisen.notesapp.presentation.viewmodel.NoteEditFragmentViewModel

class NoteEditFragment : BaseFragment<NoteEditFragmentViewModel>() {

    companion object {
        const val KEY = "Note"
    }

    override fun createViewModel(): Lazy<NoteEditFragmentViewModel> = lazy {
        ViewModelProvider.NewInstanceFactory().create(NoteEditFragmentViewModel::class.java)
    }

    override var layoutId: Int = R.layout.fragment_note_update

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.arguments.let {
            viewModel.perform(HaveNoteFromBundle(it?.getParcelable(KEY)))
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener { childFragmentManager.popBackStack() }
        initListeners()
    }

    override fun observeLiveData() {
        viewModel.state().observe(viewLifecycleOwner, Observer {
            when (it) {
                is CreateNoteState -> {
                    saveNoteButton.visibility = VISIBLE
                }
                is EditNoteState -> {
                    saveNoteButton.visibility = VISIBLE
                    titleEditText.setText(it.note?.noteTitle)
                    bodyEditText.setText(it.note?.noteBody)
                }
                is SuccessState -> {
                    saveNoteButton.visibility = GONE
                }
            }
        })
    }

    private fun initListeners() {
        saveNoteButton.setOnClickListener {
            viewModel.perform(
                ClickSaveNoteButton(titleEditText.text.toString(), bodyEditText.text.toString())
            )
        }
    }
}