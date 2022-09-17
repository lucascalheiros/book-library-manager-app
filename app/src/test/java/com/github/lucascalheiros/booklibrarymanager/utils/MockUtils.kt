package com.github.lucascalheiros.booklibrarymanager.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun getRandomString(length: Int = 10): String {
    val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return List(length) { charset.random() }
        .joinToString("")
}

fun getRandomLocalDateTime(): LocalDateTime {
    return LocalDateTime.of(
        LocalDate.now(),
        LocalTime.of((0..23).random(), (0..59).random(), (0..59).random())
    )
}