package com.example.pratham_chikitseappoffline

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Standard Activity for Hospital Finder.
 * Uses a default constructor for 100% compatibility with older APIs.
 */
class HospitalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital)
        
        // Hospital data is static in the XML as requested for offline mode
    }
}
