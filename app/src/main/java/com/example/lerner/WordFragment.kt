package com.example.lerner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView


class WordFragment : Fragment() {

    override fun onStart() {
        super.onStart()

        val activity = requireActivity()

        val word : TextView = activity.findViewById(R.id.title)
        val definition : TextView = activity.findViewById(R.id.textDefinition)
        val synonyms : TextView = activity.findViewById(R.id.textSynonyms)
        val usageExamples : TextView = activity.findViewById(R.id.text_context)
        val genQuoteButton : androidx.appcompat.widget.AppCompatImageButton = activity.findViewById(R.id.buttonGenerateQuote)

        val wordSets = WordDb(requireContext())

//        wordSets.generateDefaultLists()
//        requireActivity().finish()

        val wordValues = WordValues(  )
//        val wordValues = WordValues( wordSets.pullRandomWordFromUnlearned() )
        word.text = wordValues.word
        definition.text = wordValues.definition
        synonyms.text = wordValues.synonyms
        usageExamples.text = wordValues.getExample()

        genQuoteButton.setOnClickListener { usageExamples.text = wordValues.getExample() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_word, container, false)
    }

    fun setRandomExample() {
        val output : TextView = requireActivity().findViewById(R.id.text_context)
//        findViewById<Button>(R.id.genButton).setOnClickListener { output.text = WordValues().getExample() }
    }

}