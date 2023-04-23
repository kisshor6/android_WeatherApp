package com.example.weatherapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var searchView : SearchView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.searchButton.setOnClickListener {
            searchView.isIconified = false
        }

        getWeather("Kathmandu")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        searchView = menu!!.findItem(R.id.search_action).actionView as SearchView
        searchView.queryHint = "Enter your city..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    getWeather(query)

                }else{
                    Toast.makeText(applicationContext, "Please Enter city", Toast.LENGTH_SHORT).show()
                }

                searchView.clearFocus()
                searchView.isIconified = true

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Collapse the SearchView
                searchView.isIconified = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getWeather(query: String) {
        val queue = Volley.newRequestQueue(this)
        binding.progressBar.visibility = View.VISIBLE
        val url = "https://weather-by-api-ninjas.p.rapidapi.com/v1/weather?city=$query"
        val headers = hashMapOf(
            "X-RapidAPI-Key" to "66a26f2d17msh5aeaa606a9441bep1453d3jsne80a418ec6f0",
            "X-RapidAPI-Host" to "weather-by-api-ninjas.p.rapidapi.com"
        )

        binding.cityName.text = query
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                val tem = response.getString("temp")
                val hum = response.getString("humidity")
                val cloud = response.getString("cloud_pct")
                val speed = response.getString("wind_speed")
                val maxTemprature = response.getString("max_temp")
                val minTemprature = response.getString("min_temp")
                val feelsLike = response.getString("feels_like")

                binding.progressBar.visibility = View.GONE
                binding.nTemp.text =  tem+ "\u2103"
                binding.humidity.text = "$hum%"
                binding.airFlow.text = "$speed km/hr"
                binding.maxTemp.text = maxTemprature + "\u2103"
                binding.minTemp.text = minTemprature + "\u2103"
                binding.feelsLike.text = feelsLike
                binding.cloudCpt.text = cloud


            },
            Response.ErrorListener { error ->
                Log.d("naveen", "$error")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }

}