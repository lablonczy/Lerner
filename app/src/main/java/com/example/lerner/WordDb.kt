package com.example.lerner

import android.content.Context
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.streams.toList

class WordDb(private val context: Context) {
    companion object {
        private const val FILENAME_LEARNED = "learnedwords.txt"
        private const val FILENAME_REMAINING = "remainingwords.txt"
    }

    private var learnedList : LinkedList<String> = loadUserWords(FILENAME_LEARNED)
    private var unlearnedList : LinkedList<String> = loadUserWords(FILENAME_REMAINING)
    private val wordset : LinkedList<String> = loadFromDb()

    private fun loadUserWords(name : String) : LinkedList<String> = LinkedList(openFile(context.filesDir, name).readLines().toList())
    private fun loadFromDb() : LinkedList<String> = context.resources.openRawResource(R.raw.fivek).bufferedReader(Charsets.ISO_8859_1).lines().toList().toLinkedList()


    fun <T> List<T>.toLinkedList() : LinkedList<T> {
        val items = LinkedList<T>()

        for(item : T in this)
            items.add(item)

        return items
    }

    private fun openFile(parent : File, child : String) : File {
        val file = File(parent, child)
        file.createNewFile()

        return file
    }

    private fun moveWord(word : String, from : LinkedList<String>, to : LinkedList<String>) {
        from.add(
            to.removeAt(to.indexOf(word))
        )

        saveUserWords()
    }

    fun pullRandomWordFromUnlearned() : String = unlearnedList[Random().nextInt(unlearnedList.size)]
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