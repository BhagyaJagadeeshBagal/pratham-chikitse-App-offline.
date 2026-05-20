package com.example.pratham_chikitseappoffline

/**
 * Single source of truth for the Step data class.
 */
data class Step(
    val stepNumber: String,
    val description: String,
    val imageResId: Int
)
