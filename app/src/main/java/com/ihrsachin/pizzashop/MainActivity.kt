package com.ihrsachin.pizzashop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.*
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection

class MainActivity : AppCompatActivity() {

    val API_URL = "https://625bbd9d50128c570206e502.mockapi.io/api/v1/pizza/1"
    var JSONString = ""

    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress_circular)

        updateOnMainThread()
    }

    private fun updateOnMainThread(){
        progressBar.visibility = VISIBLE
        CoroutineScope(IO).launch { apiRequest() }
    }



    private suspend fun apiRequest() {
        val result = getStringFromApi() // wait until job is done
        passResultOnMainThread(result)
    }

    private fun getStringFromApi(): String {
        var result = ""

        try {
            val website = URL(API_URL)
            val connection: URLConnection = website.openConnection()
            val `in` = BufferedReader(
                InputStreamReader(
                    connection.getInputStream()))

            result = `in`.readLine()

            `in`.close()
        } catch (e: Exception) {  }


        return result
    }
    private suspend fun passResultOnMainThread(result: String) {
        withContext (Main) {
            progressBar.visibility = GONE
            JSONString = result
        }
    }


}