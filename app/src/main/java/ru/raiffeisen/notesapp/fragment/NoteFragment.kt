package ru.raiffeisen.notesapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notes.*
import ru.raiffeisen.notesapp.R
import ru.raiffeisen.notesapp.helper.SwipeTouchCallback
import ru.raiffeisen.notesapp.item.NoteItem
import ru.raiffeisen.notesapp.model.Note
import ru.raiffeisen.notesapp.state.NotesFragmentState
import ru.raiffeisen.notesapp.view_model.NoteFragmentViewModel

class NoteFragment : BaseFragment<NoteFragmentViewModel>() {

    private val section = Section()
    private val adapter = GroupAdapter<GroupieViewHolder>().apply { add(section) }
    private var notesMutableList: MutableList<Note> = mutableListOf()

    override fun createViewModel(): Lazy<NoteFragmentViewModel> = lazy {
        ViewModelProvider.NewInstanceFactory().create(NoteFragmentViewModel::class.java)
    }

    override var layoutId: Int = R.layout.fragment_notes

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        notesRecycler.layoutManager = linearLayoutManager
        notesRecycler.adapter = adapter
        requireActivity().toolbarMainActivity.navigationIcon = null

        adapter.setOnItemClickListener(onNoteClickListener())

        createNoteFloatActionButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, NoteEditFragment())
                .addToBackStack("Note")
                .commit()
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.getAllNotes()
        }

        ItemTouchHelper(touchCallback).attachToRecyclerView(notesRecycler)
    }

    private fun onNoteClickListener() = OnItemClickListener { item, _ ->
        val note = (adapter.getItem(adapter.getAdapterPosition(item)) as NoteItem).note
        val fragment = NoteEditFragment()
        fragment.arguments =
            putNoteInBundle(note)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack("Note")
            .commit()
    }

    private fun putNoteInBundle(note: Note): Bundle {
        val bundle = Bundle()
        bundle.putParcelable("Note", note)
        return bundle
    }

    override fun observeLiveData() {
        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NotesFragmentState.NotesLoadedState -> handleSuccess(it.notes)
            }
        })
    }

    private fun handleSuccess(notes: List<Note>) {
        if (notes.size - section.itemCount == 1) {
            section.add(NoteItem(notes[notes.size - 1]))
            notesMutableList.add(notes[notes.size - 1])
        } else {
            notes.forEach {
                section.add(NoteItem(it))
                notesMutableList.add(it)
            }
        }
        swipeRefresh.isRefreshing = false
    }

    private val touchCallback: SwipeTouchCallback by lazy {
        object : SwipeTouchCallback() {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Log.d("swipe", "onMove")
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.getItem(viewHolder.adapterPosition)
                viewModel.deleteNote(notesMutableList[viewHolder.adapterPosition])
                section.remove(item)
            }
        }

    }
}