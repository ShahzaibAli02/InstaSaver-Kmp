package com.clipsaver.quickreels.data.local

import platform.Foundation.NSUserDefaults

class IOSLocalSharedPref : LocalSharedPref {

    private val defaults = NSUserDefaults.standardUserDefaults()

    override fun saveString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    override fun saveInt(key: String, value: Int) {
        defaults.setInteger(value.toLong(), forKey = key)
    }

    override fun saveLong(key: String, value: Long) {
        defaults.setInteger(value, forKey = key)
    }

    override fun saveBool(key: String, value: Boolean) {
        defaults.setBool(value, forKey = key)
    }

    override fun getString(key: String, defaultValue: String): String {
        return defaults.stringForKey(key) ?: defaultValue
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return defaults.integerForKey(key).toInt()
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return defaults.integerForKey(key)
    }

    override fun getBool(key: String, defaultValue: Boolean): Boolean {
        return defaults.boolForKey(key)
    }
}
