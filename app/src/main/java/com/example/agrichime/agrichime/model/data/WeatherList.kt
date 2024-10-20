package com.example.agrichime.agrichime.model.data

data class WeatherList(val main: WeatherMain, val weather:List<Weather>, val dt_txt:String, val wind: WeatherWind)