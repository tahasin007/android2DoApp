package com.example.tipscalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tipscalculator.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener{
            calculateTips()
        }
    }

    private fun calculateTips() {
        val stringTextField = binding.costOfService.text.toString()
        val cost = stringTextField.toDoubleOrNull()

        if(cost == null){
            displayTip(0.0)
            return
        }

        val tipPercentage  = when(binding.tipOptions.checkedRadioButtonId){
            R.id.option_twenty_percent -> 0.2
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
        var tip = cost * tipPercentage

        if(binding.roundUpSwitch.isChecked){
            tip = ceil(tip)
        }

        displayTip(tip)
    }

    private fun displayTip(tip : Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }
}