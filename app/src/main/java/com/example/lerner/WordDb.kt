package com.example.lerner

import android.content.Context
import java.io.File
import java.util.*
import kotlin.streams.toList

class WordDb(private val context: Context) {
    companion object {
        private const val FILENAME_LEARNED = "learnedwords.txt"
        private const val FILENAME_REMAINING = "remainingwords.txt"
    }

    private var learnedList : LinkedList<String> = loadUserWords(FILENAME_LEARNED)
    private var unlearnedList : LinkedList<String> = loadUserWords(FILENAME_REMAINING)
    private val wordset : LinkedList<String> = loadFromDb()

    private fun loadUserWords(name : String) : LinkedList<String> = File(context.filesDir, name).readLines() as LinkedList<String>
    private fun loadFromDb() : LinkedList<String> = context.resources.openRawResource(R.raw.testdb).bufferedReader(Charsets.ISO_8859_1).lines().toList() as LinkedList<String>

    private fun moveWord(word : String, from : LinkedList<String>, to : LinkedList<String>) {
        from.add(
            to.removeAt(to.indexOf(word))
        )

        saveUserWords()
    }

    fun learnWord(word : String) = moveWord(word, unlearnedList, learnedList)
    fun forgetWord(word : String) = moveWord(word, learnedList, unlearnedList)

    fun saveUserWords() {
            File(context.filesDir, FILENAME_REMAINING).writeText(unlearnedList.joinToString("\n"))
            File(context.filesDir, FILENAME_LEARNED).writeText(learnedList.joinToString("\n"))
    }

    fun generateDefaultLists() {
        unlearnedList = loadFromDb()
        learnedList.clear()

        saveUserWords()
    }


}