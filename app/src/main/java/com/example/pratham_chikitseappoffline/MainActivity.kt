package com.example.pratham_chikitseappoffline

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.util.*

class MainActivity : AppCompatActivity() {

    private var currentLanguage: String = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Load language preference
        val sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        currentLanguage = sharedPref.getString("My_Lang", "en") ?: "en"
        setLocale(currentLanguage)

        setContentView(R.layout.activity_main)

        setupLanguageSpinner()
        setupEmergencyTiles()


        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.btnHospitalFinder).setOnClickListener {
            startActivity(Intent(this, HospitalActivity::class.java))
        }
    }

    private fun setupLanguageSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinnerLanguage)
        val languages = arrayOf(getString(R.string.english), getString(R.string.kannada))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(if (currentLanguage == "kn") 1 else 0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLang = if (position == 1) "kn" else "en"
                if (selectedLang != currentLanguage) {
                    saveLanguage(selectedLang)
                    // Refresh Dashboard with new language
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupEmergencyTiles() {
        // Map of Card ID to (Display Name Resource, Icon, Constant Key)
        val emergencies = mapOf(
            R.id.cardSnakeBite to Triple(R.string.snake_bite, android.R.drawable.ic_dialog_alert, "Snake Bite"),
            R.id.cardHeartAttack to Triple(R.string.heart_attack, android.R.drawable.ic_dialog_info, "Heart Attack"),
            R.id.cardFracture to Triple(R.string.fracture, android.R.drawable.ic_menu_edit, "Fracture"),
            R.id.cardChoking to Triple(R.string.choking, android.R.drawable.ic_menu_help, "Choking"),
            R.id.cardBurns to Triple(R.string.burns, android.R.drawable.ic_menu_report_image, "Burns"),
            R.id.cardAllergy to Triple(R.string.allergic_reaction, android.R.drawable.ic_menu_info_details, "Allergic Reaction"),
            R.id.cardStroke to Triple(R.string.stroke, android.R.drawable.ic_lock_idle_alarm, "Stroke"),
            R.id.cardPoisoning to Triple(R.string.poisoning, android.R.drawable.ic_delete, "Poisoning"),
            R.id.cardBleeding to Triple(R.string.bleeding, android.R.drawable.ic_notification_overlay, "Bleeding"),
            R.id.cardHeatStroke to Triple(R.string.heat_stroke, android.R.drawable.btn_star_big_on, "Heat Stroke"),
            R.id.cardDrowning to Triple(R.string.drowning, android.R.drawable.ic_menu_slideshow, "Drowning"),
            R.id.cardElectricShock to Triple(R.string.electric_shock, android.R.drawable.ic_lock_power_off, "Electric Shock"),
            R.id.cardFainting to Triple(R.string.fainting, android.R.drawable.ic_menu_view, "Fainting"),
            R.id.cardDogBite to Triple(R.string.dog_bite, android.R.drawable.ic_menu_my_calendar, "Dog Bite"),
            R.id.cardAsthma to Triple(R.string.asthma_attack, android.R.drawable.ic_menu_directions, "Asthma Attack"),
            R.id.cardSeizures to Triple(R.string.seizures, android.R.drawable.ic_popup_reminder, "Seizures"),
            R.id.cardHeadInjury to Triple(R.string.head_injury, android.R.drawable.ic_menu_sort_by_size, "Head Injury"),
            R.id.cardNosebleed to Triple(R.string.nosebleed, android.R.drawable.ic_menu_send, "Nosebleed"),
            R.id.cardInsectSting to Triple(R.string.insect_sting, android.R.drawable.ic_menu_gallery, "Insect Sting"),
            R.id.cardDiabetic to Triple(R.string.diabetic_emergency, android.R.drawable.ic_menu_agenda, "Diabetic Emergency")
        )

        for ((id, info) in emergencies) {
            val card = findViewById<CardView>(id) ?: continue
            card.findViewById<TextView>(R.id.tvEmergencyName).text = getString(info.first)
            card.findViewById<ImageView>(R.id.ivEmergencyIcon).setImageResource(info.second)
            card.setOnClickListener { openInstructions(info.third) }
        }
    }

    private fun openInstructions(typeKey: String) {
        val intent = Intent(this, InstructionsActivity::class.java)
        intent.putExtra("EMERGENCY_TYPE", typeKey)
        startActivity(intent)
    }

    private fun saveLanguage(lang: String) {
        val sharedPref = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("My_Lang", lang)
            apply()
        }
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
 