package com.github.lucascalheiros.booklibrarymanager.model.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class Converters {
    @TypeConverter
    fun from(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(it),
                TimeZone.getDefault().toZoneId()
            )
        }
    }

    @TypeConverter
    fun to(value: LocalDateTime?): Long? {
        return value?.atZone(TimeZone.getDefault().toZoneId())?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun from(value: String?): Map<String, String>? {
        val type: Type = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun to(value: Map<String, String>?): String {
        return Gson().toJsonTree(value).asString
    }


}