package com.example.agrichime.agrichime.viewmodel

import androidx.lifecycle.LiveData

interface WeatherListener {
    fun onSuccess(authRepo: LiveData<String>)
}