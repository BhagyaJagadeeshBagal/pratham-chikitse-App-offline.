package com.example.pratham_chikitseappoffline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * StepAdapter for ViewPager2.
 * Uses the standard constructor for maximum compatibility.
 */
class StepAdapter(private val steps: List<Step>) : RecyclerView.Adapter<StepAdapter.StepViewHolder>() {

    class StepViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivStepImage: ImageView = view.findViewById(R.id.ivStepImage)
        val tvStepNumber: TextView = view.findViewById(R.id.tvStepNumber)
        val tvStepDescription: TextView = view.findViewById(R.id.tvStepDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_instruction_step, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = steps[position]
        holder.tvStepNumber.text = step.stepNumber
        holder.tvStepDescription.text = step.description
        holder.ivStepImage.setImageResource(step.imageResId)
    }

    override fun getItemCount(): Int = steps.size
}
