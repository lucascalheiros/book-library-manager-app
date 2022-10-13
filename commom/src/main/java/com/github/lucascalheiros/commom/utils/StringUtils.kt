package com.github.lucascalheiros.commom.utils

object StringUtils {
    @JvmStatic
    fun formatFloatToPercent(percentage: Float): String {
        val output = if (percentage <= 100) {
            if (percentage > 1) {
                percentage
            } else if (percentage > 0) {
                percentage * 100
            } else 0
        } else {
            100
        }
        return "${output.toInt()}%"
    }
}