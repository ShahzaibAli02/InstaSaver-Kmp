package com.clipsaver.quickreels.data.local

interface LocalSharedPref {
    fun saveString(key: String, value: String)
    fun saveInt(key: String, value: Int)
    fun saveLong(key: String, value: Long)
    fun saveBool(key: String, value: Boolean)

    fun getString(key: String, defaultValue: String = ""): String
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun getLong(key: String, defaultValue: Long = 0L): Long
    fun getBool(key: String, defaultValue: Boolean = false): Boolean
}