package com.example.weatherapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ReterofitInstance {
    private  val baseurl = "https://api.weatherapi.com"
    private fun getInstance() : Retrofit{
        return  Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val weatherApi : WeatherApi = getInstance().create(WeatherApi::class.java)
}