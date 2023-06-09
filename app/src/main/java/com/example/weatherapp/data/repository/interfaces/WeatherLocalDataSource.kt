package com.example.weatherapp.data.repository.interfaces

import com.example.weatherapp.data.repository.entity.WeatherDBEntity

interface WeatherLocalDataSource {
    fun getWeatherByCityId(cityId: String): WeatherDBEntity

    fun insertWeather(weather: WeatherDBEntity)

    fun getLastRemoteFetch(cityId: String): Long?
}