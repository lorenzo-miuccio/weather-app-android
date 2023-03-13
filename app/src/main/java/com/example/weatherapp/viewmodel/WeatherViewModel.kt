package com.example.weatherapp.viewmodel

import androidx.lifecycle.*
import com.example.weatherapp.datasource.WeatherApi
import com.example.weatherapp.model.City
import com.example.weatherapp.model.WeatherFetchState
import com.example.weatherapp.model.citiesList
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepo: WeatherRepository): ViewModel() {

    private val _selectedCity: MutableLiveData<City> = MutableLiveData()
    val selectedCity: LiveData<City>
    get() = _selectedCity

    private val _weatherFetchState: MutableStateFlow<WeatherFetchState> = MutableStateFlow(WeatherFetchState.Loading)
    val weatherFetchState: StateFlow<WeatherFetchState>
    get() = _weatherFetchState

    init {
        _selectedCity.value = weatherRepo.getSelectedCity()

        refreshWeather()
    }

    fun updateSelectedCity(newCity: City) {

        weatherRepo.setSelectedCity(newCity)
        _selectedCity.value = weatherRepo.getSelectedCity()

        refreshWeather()
    }

    fun refreshWeather() {
        viewModelScope.launch {
            try {
                _weatherFetchState.value = WeatherFetchState.Loading
                val cityId = _selectedCity.value!!.id
                val res = weatherRepo.getWeatherByCityId(_selectedCity.value!!.id)

                _weatherFetchState.value = WeatherFetchState.Success(res)
            } catch (e: Exception) {
                _weatherFetchState.value = WeatherFetchState.Error(e)
            }
        }
    }

    class Factory(private val weatherRepo: WeatherRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WeatherViewModel(weatherRepo) as T
        }
    }
}