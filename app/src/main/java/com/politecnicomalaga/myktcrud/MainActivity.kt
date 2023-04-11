package com.politecnicomalaga.myktcrud

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    private val TextInputEditText textInputUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
SplashScreen.installSplashScreen()
        setContentView(R.layout.activity_main)
    }
}