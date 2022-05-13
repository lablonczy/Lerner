package com.example.lerner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextSwitcher
import android.widget.TextView
import java.util.*
import org.jsoup.Jsoup as web

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun examplesGetter(word : String = "angeklagten") {
        val examples : LinkedList<String> = getExamples(word)

        val output : TextView = findViewById(R.id.output)
        findViewById<Button>(R.id.genButton).setOnClickListener { output.text = getExample(examples) }
    }

    fun getExample(exampleSet : LinkedList<String>) : String {
        return exampleSet[Random().nextInt(exampleSet.size)]

    }

    fun getExamples(word : String, query : String = "table#result td.r.sp") : LinkedList<String> {
        val sentences = LinkedList<String>()

        Thread {

            val doc =
                web.connect("https://dict.tu-chemnitz.de/dings.cgi?o=3020;service=en-de;iservice=de-en-ex;query=$word")
                    .get()

            val results = doc.select(query)

            for (i in 0 until results.size step 2)
                sentences.add(results[i].text())

        }.apply {
            start()
            join()
        }
//        sentences.forEach { println(it)}

        return sentences
    }
}