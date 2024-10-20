package com.example.agrichime.agrichime.viewmodel

import androidx.lifecycle.LiveData

interface AuthListener {

    fun onStarted()
    fun onSuccess(authRepo: LiveData<String>)
    fun onFailure(message: String)
}