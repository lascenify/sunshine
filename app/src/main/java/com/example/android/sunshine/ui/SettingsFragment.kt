package com.example.android.sunshine.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.android.sunshine.R

class SettingsFragment :PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener{

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        val sharedPreferences = preferenceScreen.sharedPreferences
        val prefScreen = preferenceScreen
        val count = prefScreen.preferenceCount

        for (i in 0 until count) {
            val preference = prefScreen.getPreference(i)
            if (preference !is CheckBoxPreference) {
                val value = sharedPreferences.getString(preference.key, "")!!
                setPreferenceSummary(preference, value)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

    }

    private fun setPreferenceSummary(preference:Preference, value: String){
       if (preference is ListPreference) {
           val listPreference: ListPreference = preference
           val prefIndex: Int = listPreference.findIndexOfValue(value)
           if (prefIndex >= 0) {
               preference.setSummary(listPreference.entries[prefIndex])
           }
       } else { // For other preferences, set the summary to the value's simple string representation.
           preference.summary = value
       }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // Figure out which preference was changed
        val preference: Preference = findPreference(key!!)!!
        if (preference !is CheckBoxPreference) {
            val value = sharedPreferences!!.getString(preference.key, "")!!
            setPreferenceSummary(preference, value)
        }
    }
}