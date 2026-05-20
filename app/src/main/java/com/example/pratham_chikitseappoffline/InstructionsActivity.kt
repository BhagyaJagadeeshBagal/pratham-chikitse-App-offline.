package com.example.pratham_chikitseappoffline

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import java.util.*

class InstructionsActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var tts: TextToSpeech
    private var steps = listOf<Step>()
    private var isAudioPlaying = false
    private var emergencyType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions)

        emergencyType = intent.getStringExtra("EMERGENCY_TYPE") ?: "Snake Bite"
        setupSteps(emergencyType)

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = StepAdapter(steps)

        tts = TextToSpeech(this, this)

        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnPrev = findViewById<Button>(R.id.btnPrev)
        val btnAudio = findViewById<Button>(R.id.btnAudio)
        val btnShare = findViewById<Button>(R.id.btnShare)

        btnNext.setOnClickListener {
            if (viewPager.currentItem < steps.size - 1) {
                viewPager.currentItem += 1
            } else {
                // Instructions complete, go to Home Page
                finish()
            }
        }

        btnPrev.setOnClickListener {
            if (viewPager.currentItem > 0) {
                viewPager.currentItem -= 1
            }
        }

        btnAudio.setOnClickListener {
            if (isAudioPlaying) {
                tts.stop()
                btnAudio.text = getString(R.string.audio_mode)
                isAudioPlaying = false
            } else {
                speakCurrentStep()
                btnAudio.text = getString(R.string.stop_audio)
                isAudioPlaying = true
            }
        }

        btnShare.setOnClickListener {
            shareEmergencyInfo()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == steps.size - 1) {
                    btnNext.text = getString(R.string.finish)
                } else {
                    btnNext.text = getString(R.string.next)
                }

                if (isAudioPlaying) {
                    speakCurrentStep()
                }
            }
        })
    }

    private fun setupSteps(type: String) {
        steps = when (type) {
            "Snake Bite" -> createSteps(R.string.sb_step1, R.string.sb_step2, R.string.sb_step3, R.string.sb_step4)
            "Heart Attack" -> createSteps(R.string.ha_step1, R.string.ha_step2, R.string.ha_step3, R.string.ha_step4)
            "Fracture" -> createSteps(R.string.fr_step1, R.string.fr_step2, R.string.fr_step3, R.string.fr_step4)
            "Choking" -> createSteps(R.string.ch_step1, R.string.ch_step2, R.string.ch_step3, R.string.ch_step4)
            "Burns" -> createSteps(R.string.bu_step1, R.string.bu_step2, R.string.bu_step3, R.string.bu_step4)
            "Allergic Reaction" -> createSteps(R.string.ar_step1, R.string.ar_step2, R.string.ar_step3, R.string.ar_step4)
            "Stroke" -> createSteps(R.string.st_step1, R.string.st_step2, R.string.st_step3, R.string.st_step4)
            "Poisoning" -> createSteps(R.string.po_step1, R.string.po_step2, R.string.po_step3, R.string.po_step4)
            "Bleeding" -> createSteps(R.string.bl_step1, R.string.bl_step2, R.string.bl_step3, R.string.bl_step4)
            "Heat Stroke" -> createSteps(R.string.hs_step1, R.string.hs_step2, R.string.hs_step3, R.string.hs_step4)
            "Drowning" -> createSteps(R.string.dr_step1, R.string.dr_step2, R.string.dr_step3, R.string.dr_step4)
            "Electric Shock" -> createSteps(R.string.es_step1, R.string.es_step2, R.string.es_step3, R.string.es_step4)
            "Fainting" -> createSteps(R.string.fa_step1, R.string.fa_step2, R.string.fa_step3, R.string.fa_step4)
            "Dog Bite" -> createSteps(R.string.db_step1, R.string.db_step2, R.string.db_step3, R.string.db_step4)
            "Asthma Attack" -> createSteps(R.string.as_step1, R.string.as_step2, R.string.as_step3, R.string.as_step4)
            "Seizures" -> createSteps(R.string.se_step1, R.string.se_step2, R.string.se_step3, R.string.se_step4)
            "Head Injury" -> createSteps(R.string.hi_step1, R.string.hi_step2, R.string.hi_step3, R.string.hi_step4)
            "Nosebleed" -> createSteps(R.string.nb_step1, R.string.nb_step2, R.string.nb_step3, R.string.nb_step4)
            "Insect Sting" -> createSteps(R.string.is_step1, R.string.is_step2, R.string.is_step3, R.string.is_step4)
            "Diabetic Emergency" -> createSteps(R.string.de_step1, R.string.de_step2, R.string.de_step3, R.string.de_step4)
            else -> listOf(Step("Step 1", "Keep calm and seek help.", android.R.drawable.ic_dialog_alert))
        }
    }

    private fun createSteps(s1: Int, s2: Int, s3: Int, s4: Int): List<Step> {
        return listOf(
            Step("Step 1", getString(s1), android.R.drawable.ic_dialog_alert),
            Step("Step 2", getString(s2), android.R.drawable.ic_dialog_info),
            Step("Step 3", getString(s3), android.R.drawable.ic_menu_edit),
            Step("Step 4", getString(s4), android.R.drawable.ic_lock_idle_alarm)
        )
    }

    private fun shareEmergencyInfo() {
        val shareBody = "Emergency: $emergencyType\nSteps:\n" +
                steps.joinToString("\n") { "${it.stepNumber}: ${it.description}" }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareBody)
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun speakCurrentStep() {
        if (::tts.isInitialized) {
            tts.speak(steps[viewPager.currentItem].description, TextToSpeech.QUEUE_FLUSH, null, "ID")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(if (Locale.getDefault().language == "kn") Locale("kn", "IN") else Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
