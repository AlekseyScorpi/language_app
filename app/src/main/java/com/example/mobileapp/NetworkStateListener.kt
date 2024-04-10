package com.example.mobileapp

interface NetworkStateListener {
    fun onNetworkConnected()
    fun onNetworkDisconnected()
}