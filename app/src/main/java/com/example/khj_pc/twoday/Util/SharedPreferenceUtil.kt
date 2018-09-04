package com.example.khj_pc.twoday.Util

import android.content.Context

object SharedPreferenceUtil {
    fun getPreference(context: Context, key: String): String? {
        val sharedPreferences = context.getSharedPreferences("gaonnuri", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }

    fun savePreferences(context: Context, key: String, data: String) {
        val sharedPreferences = context.getSharedPreferences("gaonnuri", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, data)
        editor.commit()
    }

    fun removePreferences(context: Context, key: String) {
        val pref = context.getSharedPreferences("gaonnuri", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.remove(key)
        editor.commit()
    }
}