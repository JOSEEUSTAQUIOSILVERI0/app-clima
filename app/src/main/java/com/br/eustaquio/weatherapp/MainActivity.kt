package com.br.eustaquio.weatherapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.br.eustaquio.weatherapp.network.RetrofitInstance
import com.br.eustaquio.weatherapp.network.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var editCity: EditText
    private lateinit var btnFetchWeather: Button
    private lateinit var tvWeatherInfo: TextView
    private lateinit var btnExit: Button  // Adicionando a referência do botão de sair

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editCity = findViewById(R.id.editCity)
        btnFetchWeather = findViewById(R.id.btnFetchWeather)
        tvWeatherInfo = findViewById(R.id.tvWeatherInfo)
        btnExit = findViewById(R.id.btnExit)  // Inicializando o botão de sair

        btnFetchWeather.setOnClickListener {
            val city = editCity.text.toString()
            if (city.isNotEmpty()) {
                getWeather(city)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }

        btnExit.setOnClickListener {
            finish()  // Finaliza a atividade atual, fechando o aplicativo
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getWeather(city: String) {
        val apiKey = "2b5023884543e7d868742cfff92ca1eb"  // Substitua pela sua chave de API
        RetrofitInstance.api.getWeather(city, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    val temp = weatherResponse?.main?.temp?.minus(273.15)?.let { "%.2f".format(it) } ?: "N/A"
                    val description = weatherResponse?.weather?.get(0)?.description ?: "N/A"
                    val humidity = weatherResponse?.main?.humidity ?: "N/A"

                    tvWeatherInfo.text = "Condição do tempo em $city: $description, Temperatura: $temp°C, Humidade: $humidity%"
                } else {
                    tvWeatherInfo.text = "Erro: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                tvWeatherInfo.text = "Erro: ${t.message}"
            }
        })
    }
}
