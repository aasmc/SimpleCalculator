package com.example.simplecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.simplecalculator.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    /**
     * Shows if the last character is a digit
     */
    var lastNumeric = false

    /**
     * Shows if the last character is a dot
     */
    var lastDot = false

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    }

    /**
     * Appends the text value of a button to the resulting text view
     * Sets value of lastNumeric flag to true
     */
    fun onDigit(view: View) {
        val button = view as Button
        binding.tvResult.append(button.text)
        lastNumeric = true

    }

    /**
     * Appends the operator to the resulting textView only if the previous character is a digit
     * And only if there're no more operators
     * Resets the lastDot and lastNumeric flags to false to allow other number to be input
     */
    fun onOperator(view: View) {
        // if the last character is numeric and only a single operator is added
        if (lastNumeric && !isOperatorAdded(binding.tvResult.text.toString())) {
            binding.tvResult.append((view as Button).text)
            lastDot = false
            lastNumeric = false
        }
    }

    /**
     * Checks if the operator has been added to the resulting textView
     * Initial minus is not considered as the operator
     */
    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) {
            false
        } else {
            value.contains("[-/*+]".toRegex())
        }
    }

    /**
     * Clears the resulting textView and resets lastNumeric and lastDot flags
     */
    fun onClear(view: View) {
        binding.tvResult.text = ""
        lastNumeric = false
        lastDot = false
    }

    /**
     * Appends a '.' to the resulting textView only if it contains a digit as the last character
     * Sets the flag of lastDot to true so that to prohibit using it again
     */
    fun onDecimalPoint(view: View) {
        if (lastNumeric && !lastDot) {
            binding.tvResult.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    /**
     * Removes .0 from the string
     */
    private fun removeZeroAfterDot(result: String): String {
        var value = result
        if (result.contains(".0")) {
            value = result.substring(0, result.length - 2) // discard last two characters
        }
        return value
    }

    /**
     * Calculates the result of operations
     * Supports simple operations: + - / *
     * Only two operands are allowed
     * Only one operator is allowed
     */
    fun onEqual(view: View) {
        // only if the last character is numeric
        if (lastNumeric) {
            var tvValue = binding.tvResult.text.toString()
            var prefix = ""
            try {
                if (tvValue.startsWith("-")) {
                    prefix = "-"
                    tvValue = tvValue.substring(1) // disregard the initial minus
                }
                when {
                    // minus
                    tvValue.contains("-") -> {
                        val operands = tvValue.split("-")
                        var first = operands[0]
                        val second = operands[1]
                        // if prefix contains minus add it to the first operand
                        if (prefix.isNotEmpty()) {
                            first = prefix + first
                        }
                        binding.tvResult.text = removeZeroAfterDot ((first.toDouble() - second.toDouble()).toString())
                    }
                    // plus
                    tvValue.contains("+") -> {
                        val operands = tvValue.split("+")
                        var first = operands[0]
                        val second = operands[1]
                        // if prefix contains minus add it to the first operand
                        if (prefix.isNotEmpty()) {
                            first = prefix + first
                        }
                        binding.tvResult.text = removeZeroAfterDot ((first.toDouble() + second.toDouble()).toString())
                    }
                    // multiply
                    tvValue.contains("*") -> {
                        val operands = tvValue.split("*")
                        var first = operands[0]
                        val second = operands[1]
                        // if prefix contains minus add it to the first operand
                        if (prefix.isNotEmpty()) {
                            first = prefix + first
                        }
                        binding.tvResult.text = removeZeroAfterDot ((first.toDouble() * second.toDouble()).toString())
                    }
                    // divide
                    else -> {
                        val operands = tvValue.split("/")
                        var first = operands[0]
                        val second = operands[1]
                        // if prefix contains minus add it to the first operand
                        if (prefix.isNotEmpty()) {
                            first = prefix + first
                        }
                        binding.tvResult.text = removeZeroAfterDot ((first.toDouble() / second.toDouble()).toString())
                    }
                }
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }
}