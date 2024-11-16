package com.br.eustaquio.weatherapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String = "pt_br"
    ): Call<WeatherResponse>
}
