package com.example.weatherapp

import android.util.Log
import android.widget.ListView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Constant.apikey
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.ReterofitInstance
import com.example.weatherapp.api.WeatherModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class WeatherViewModel : ViewModel(){
    private val weatherApi  = ReterofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : MutableLiveData<NetworkResponse<WeatherModel>> = _weatherResult

   fun getdata(city:String){
       _weatherResult.value = NetworkResponse.Loading
       viewModelScope.launch {
           try {
               val response =   weatherApi.getWeather(apikey,city)
               if (response.isSuccessful){
                   response.body() ?.let{
                       _weatherResult.value = NetworkResponse.Success(it)
                   }
               }else{
                   _weatherResult.value = NetworkResponse.Error("Failed to Load")
               }
           }catch (e:Exception){
               _weatherResult.value = NetworkResponse.Error("Failed to Load")
           }

       }


   }
}