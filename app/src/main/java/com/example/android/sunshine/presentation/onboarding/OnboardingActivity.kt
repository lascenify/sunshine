package com.example.android.sunshine.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.android.sunshine.R

class OnboardingActivity: FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_activity)
        supportFragmentManager.beginTransaction()
            .replace(R.id.onboarding_fragment, OnboardingFragment()).commitAllowingStateLoss()
    }
}