package com.example.lerner

import org.jsoup.Jsoup
import java.util.*

class WordValues(val word : String = "angeklagten") {
    val definition : String = getData { getDefinitionSet() }!!
    val examples : LinkedList<String> = getData { getExampleSet() }!!
    val synonyms : String = getData { getSynonymSet() }!!

    /**
     * use this to generalize running any network thread and getting its result of any type. requires ? bc result cannot be returned without being outside of the thread, and outside of the thread it needs to b initialized
     */
    fun <T> getData(getter : () -> T) : T? {
        var result : T? = null

        Thread {

            result = getter()

        }.apply {
            start()
            join()
        }

        return result
    }

    fun getExampleSet() : LinkedList<String> {
        val sentences = LinkedList<String>()

        val doc =
            Jsoup.connect("https://dict.tu-chemnitz.de/dings.cgi?o=3020;service=en-de;iservice=de-en-ex;query=$word")
                .get()

        val results = doc.select("table#result td.r.sp")

        for (i in 0 until results.size step 2)
            sentences.add(results[i].text())

        return sentences
    }

    private fun getSynonymSet() : String {
        val doc = Jsoup.connect("https://www.openthesaurus.de/synonyme/$word").get()
        return doc.select(".result")[0].text().replace("·","")
    }

    fun getDefinitionSet() : String {
        //https://en.langenscheidt.com/german-english/search?term=jemandem&q_cat=%2Fgerman-english%2F to reduce word to basic form #lemma-hidden first result
        //https://www.dwds.de/wb/jemand search .dwdswb-belegtext to get def of any given word after reduction

        val wordReducingPage = Jsoup.connect("https://en.langenscheidt.com/german-english/search?term=$word&q_cat=%2Fgerman-english%2F").get()

        var reducedWord = wordReducingPage.select(".lemma-hidden")[0].text()
        reducedWord = reducedWord.substring(1, reducedWord.length-1)

        val definingPage = Jsoup.connect("https://www.dwds.de/wb/$reducedWord").get()

        return definingPage.select(".dwdswb-definition").text()
    }

    fun getExample() : String { return examples[Random().nextInt(examples.size)] }

    fun getDefinitionSetOld() : String {
        //https://en.langenscheidt.com/german-english/search?term=jemandem&q_cat=%2Fgerman-english%2F to reduce word to basic form #lemma-hidden first result
        //https://www.dwds.de/wb/jemand search .dwdswb-belegtext to get def of any given word after reduction

        var def = ""

        Thread {
            val wordReducingPage = Jsoup.connect("https://en.langenscheidt.com/german-english/search?term=$word&q_cat=%2Fgerman-english%2F").get()

            var reducedWord = wordReducingPage.select(".lemma-hidden")[0].text()
            reducedWord = reducedWord.substring(1, reducedWord.length-1)

            val definingPage = Jsoup.connect("https://www.dwds.de/wb/$reducedWord").get()

            def = definingPage.select(".dwdswb-definition").text()

        }.apply {
            start()
            join()
        }

        return def
    }

    fun getSynonymSetOld(): String {
        //https://www.openthesaurus.de/synonyme/jemand #wiktionaryItem

        var synonyms = ""

        Thread {
            val doc = Jsoup.connect("https://www.openthesaurus.de/synonyme/$word").get()

            synonyms = doc.select(".result")[0].text().replace("·","")


        }.apply {
            start()
            join()
        }

        return synonyms
    }

    fun getExampleSetOld() : LinkedList<String> {
        val sentences = LinkedList<String>()

        Thread {

            val doc =
                Jsoup.connect("https://dict.tu-chemnitz.de/dings.cgi?o=3020;service=en-de;iservice=de-en-ex;query=$word")
                    .get()

            val results = doc.select("table#result td.r.sp")

            for (i in 0 until results.size step 2)
                sentences.add(results[i].text())

        }.apply {
            start()
            join()
        }

        return sentences
    }





}