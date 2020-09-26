package com.example.android.sunshine.presentation.onboarding

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.leanback.app.OnboardingSupportFragment
import androidx.preference.PreferenceManager
import com.example.android.sunshine.R
import com.example.android.sunshine.framework.SunshinePreferences.PREF_USER_FINISHED_ONBOARDING
import com.example.android.sunshine.presentation.base.MainActivity

class OnboardingFragment(): OnboardingSupportFragment() {
    private val screenSteps = 3

    override fun getPageTitle(pageIndex: Int): CharSequence? {
        return when (pageIndex) {
            0 -> resources.getString(R.string.onboarding_welcome_fragment)
            1 -> resources.getString(R.string.onboarding_location_fragment)
            2 -> resources.getString(R.string.onboarding_finish_fragment)
            else -> resources.getString(R.string.onboarding_finish_fragment)
        }
    }

    override fun getPageDescription(pageIndex: Int): CharSequence? {
        return when (pageIndex) {
            0 -> resources.getString(R.string.onboarding_welcome_desc)
            1 -> resources.getString(R.string.onboarding_location_desc)
            2 -> resources.getString(R.string.onboarding_finish_desc)
            else -> resources.getString(R.string.onboarding_finish_desc)
        }
    }

    @Nullable
    override fun onCreateForegroundView(
        inflater: LayoutInflater?,
        container: ViewGroup?
    ): View? {
        return null
    }

    override fun onCreateBackgroundView(
        inflater: LayoutInflater?,
        container: ViewGroup?
    ): View? {
        return null
    }

    override fun getPageCount(): Int {
        return screenSteps
    }

    override fun onCreateContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?
    ): View? {
        arrowColor = Color.GRAY
        arrowBackgroundColor = Color.YELLOW
        startButtonText = resources.getString(R.string.onboarding_start)
        dotBackgroundColor = Color.DKGRAY
        val content = ImageView(context)
        content.setImageResource(R.mipmap.ic_launcher)
        content.setScaleType(ImageView.ScaleType.CENTER_INSIDE)
        content.setPadding(0, 32, 0, 32)
        return content
    }

    override fun onFinishFragment() {
        super.onFinishFragment()
        // User has seen OnboardingSupportFragment, so mark our SharedPreferences
        // flag as completed so that we don't show our OnboardingSupportFragment
        // the next time the user launches the app.
        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putBoolean(PREF_USER_FINISHED_ONBOARDING, true)
            apply()
            startActivity(Intent(context, MainActivity::class.java))
            requireActivity().finish()
        }
    }
}