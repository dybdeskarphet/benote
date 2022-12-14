package com.ahmetardakavakci.benote.view

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ahmetardakavakci.benote.R
import com.ahmetardakavakci.benote.databinding.FragmentContentBinding
import com.ahmetardakavakci.benote.db.DatabaseNote
import com.ahmetardakavakci.benote.db.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContentFragment : Fragment() {

    // actions
    val contentToList = ContentFragmentDirections.actionContentFragmentToListFragment()

    // binding related
    private var _binding: FragmentContentBinding? = null
    private val binding get() = _binding!!

    // db
    private lateinit var db: DatabaseNote
    private lateinit var note: Note
    private var color: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseNote.getDatabase(requireContext())

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            view?.findNavController()?.navigate(contentToList)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // collapse all fabs
            deleteFab.alpha = 0f
            saveFab.alpha = 0f
            menuFab.shrink()
            colorMenu.shrink()

            // fab click listeners
            saveFab.setOnClickListener { saveFab(view) }
            deleteFab.setOnClickListener {

                CoroutineScope(Dispatchers.IO).launch() {
                    db.daoNote().delete(note)
                }

                view.findNavController().navigate(contentToList)
            }
            colorMenu.setOnClickListener { colorMenu(view) }
            menuFab.setOnClickListener { menuFab(view) }

            redSel.setOnClickListener {
                color = ContextCompat.getColor(requireContext(), R.color.red)
                binding.linearLayout.setBackgroundColor(color)
                requireActivity().window.navigationBarColor = color
            }

            blueSel.setOnClickListener {
                color = ContextCompat.getColor(requireContext(), R.color.blue)
                binding.linearLayout.setBackgroundColor(color)
                requireActivity().window.navigationBarColor = color
            }

            greenSel.setOnClickListener {
                color = ContextCompat.getColor(requireContext(), R.color.green)
                binding.linearLayout.setBackgroundColor(color)
                requireActivity().window.navigationBarColor = color
            }

            magentaSel.setOnClickListener {
                color = ContextCompat.getColor(requireContext(), R.color.magenta)
                binding.linearLayout.setBackgroundColor(color)
                requireActivity().window.navigationBarColor = color
            }

            yellowSel.setOnClickListener {
                color = ContextCompat.getColor(requireContext(), R.color.yellow)
                binding.linearLayout.setBackgroundColor(color)
                requireActivity().window.navigationBarColor = color
            }

            cyanSel.setOnClickListener {
                color = ContextCompat.getColor(requireContext(), R.color.cyan)
                binding.linearLayout.setBackgroundColor(color)
                requireActivity().window.navigationBarColor = color
            }

        }

        checkIfNew()
    }

    fun View.setMargins(
        leftMarginDp: Int? = null,
        topMarginDp: Int? = null,
        rightMarginDp: Int? = null,
        bottomMarginDp: Int? = null
    ) {
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            val params = layoutParams as ViewGroup.MarginLayoutParams
            leftMarginDp?.run { params.leftMargin = this.dpToPx(context) }
            topMarginDp?.run { params.topMargin = this.dpToPx(context) }
            rightMarginDp?.run { params.rightMargin = this.dpToPx(context) }
            bottomMarginDp?.run { params.bottomMargin = this.dpToPx(context) }
            requestLayout()
        }
    }

    fun Int.dpToPx(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
    }

    private fun checkIfNew() {
        arguments?.let { bundle ->
            val new = ContentFragmentArgs.fromBundle(bundle).new
            val choosenId = ContentFragmentArgs.fromBundle(bundle).id

            if (new) {

                with(binding){
                    titleText.setText("")
                    contentText.setText("")
                    deleteFab.visibility = View.GONE
                    menuFab.visibility = View.GONE
                    saveFab.setMargins(16,16,16,16)
                    saveFab.visibility = View.VISIBLE
                }

            } else {

                CoroutineScope(Dispatchers.IO).launch() {
                    note = db.daoNote().getById(choosenId)

                    withContext(Dispatchers.Main) {
                        with(binding) {
                            titleText.setText(note.titleString)
                            contentText.setText(note.contentString)

                            if(note.colorInt == 0) {
                                val white = ContextCompat.getColor(requireContext(), R.color.white)
                                binding.linearLayout.setBackgroundColor(white)
                                requireActivity().window.navigationBarColor = white
                            } else {
                                binding.linearLayout.setBackgroundColor(note.colorInt!!)
                                requireActivity().window.navigationBarColor = note.colorInt!!
                                color = note.colorInt!!
                            }
                        }
                    }
                }


            }

        }
    }

    private fun menuFab(view: View) {
       arguments?.let { bundle ->
           val new = ContentFragmentArgs.fromBundle(bundle).new

           if (binding.saveFab.visibility == View.INVISIBLE && binding.deleteFab.visibility == View.INVISIBLE) {
               binding.apply {
                   saveFab.visibility = View.VISIBLE
                   if (!new) {
                       deleteFab.visibility = View.VISIBLE
                   }
                   menuFab.extend()
               }
           } else {
               binding.apply {
                   saveFab.visibility = View.INVISIBLE
                   if (!new) {
                       deleteFab.visibility = View.INVISIBLE
                   }
                   menuFab.shrink()
               }
           }
       }
    }

    private fun colorMenu(view: View) {
        if (binding.colorLayout.visibility == View.INVISIBLE) {
            binding.colorLayout.visibility = View.VISIBLE
            binding.colorMenu.extend()
        } else {
            binding.colorLayout.visibility = View.INVISIBLE
            binding.colorMenu.shrink()
        }
    }

    private fun saveFab(view: View) {

        arguments?.let { bundle ->
            val new = ContentFragmentArgs.fromBundle(bundle).new
            val choosenId = ContentFragmentArgs.fromBundle(bundle).id

            val title = binding.titleText.text.toString()
            val content = binding.contentText.text.toString()

            if (new && title.isNotEmpty() && content.isNotEmpty()) {

                val note = Note(
                    null, title, content, color
                )

                CoroutineScope(Dispatchers.IO).launch {
                    db.daoNote().insert(note)
                }

                view.findNavController().navigate(contentToList)

            } else if (!new && title.isNotEmpty() && content.isNotEmpty()){
                CoroutineScope(Dispatchers.IO).launch() {
                    db.daoNote().update(choosenId, title, content, color)
                }

                view.findNavController().navigate(contentToList)

            } else {
                Toast.makeText(requireContext(), "Note cannot be empty", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}