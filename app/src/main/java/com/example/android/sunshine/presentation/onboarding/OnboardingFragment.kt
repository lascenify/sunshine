package com.example.android.sunshine.presentation.onboarding

import android.animation.*
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.leanback.app.OnboardingSupportFragment
import androidx.preference.PreferenceManager
import com.example.android.sunshine.R
import com.example.android.sunshine.framework.SunshinePreferences
import com.example.android.sunshine.framework.SunshinePreferences.PREF_USER_FINISHED_ONBOARDING
import com.example.android.sunshine.framework.SunshinePreferences.PREF_USER_FIRST_TIME
import com.example.android.sunshine.presentation.base.MainActivity

class OnboardingFragment(): OnboardingSupportFragment() {
    private val screenSteps = 2

    override fun getPageTitle(pageIndex: Int): CharSequence? {
        return when (pageIndex) {
            0 -> resources.getString(R.string.onboarding_welcome_fragment)
            1 -> resources.getString(R.string.onboarding_finish_fragment)
            else -> resources.getString(R.string.onboarding_finish_fragment)
        }
    }

    override fun getPageDescription(pageIndex: Int): CharSequence? {
        return when (pageIndex) {
            0 -> resources.getString(R.string.onboarding_welcome_desc)
            1 -> resources.getString(R.string.onboarding_finish_desc)
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
        val content = ImageView(context)
        content.setImageResource(R.drawable.ic_01d)
        content.scaleType = ImageView.ScaleType.CENTER_INSIDE
        content.setPadding(40, 60, 40, 0)
        return content
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

    override fun onCreateEnterAnimation(): Animator? {
        val content = ImageView(context)
        content.setImageResource(R.mipmap.ic_launcher)
        return ObjectAnimator.ofFloat(content, View.SCALE_X, 0.2f, 1.0f)
            .setDuration(5000)
    }

    public override fun onCreateLogoAnimation(): Animator =
        AnimatorInflater.loadAnimator(context, R.animator.lb_onboarding_logo_enter)


    override fun onPageChanged(newPage: Int, previousPage: Int) {
        val content = ImageView(context)
        // Create a fade-out animation used to fade out previousPage and, once
        // done, swaps the contentView image with the next page's image.
        val fadeOut = ObjectAnimator.ofFloat(content, View.ALPHA, 1.0f, 0.0f)
            .setDuration(16)
            .apply {
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        content.setImageResource(R.drawable.ic_logo)
                    }
                })
            }
        // Create a fade-in animation used to fade in nextPage
        val fadeIn = ObjectAnimator.ofFloat(content, View.ALPHA, 0.0f, 1.0f)
            .setDuration(16)
        // Create AnimatorSet with our fade-out and fade-in animators, and start it
        AnimatorSet().apply {
            playSequentially(fadeOut, fadeIn)
            start()
        }
    }

    override fun onFinishFragment() {
        super.onFinishFragment()
        // User has seen OnboardingSupportFragment, so mark our SharedPreferences
        // flag as completed so that we don't show our OnboardingSupportFragment
        // the next time the user launches the app.
        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putBoolean(PREF_USER_FINISHED_ONBOARDING, true)
            putBoolean(PREF_USER_FIRST_TIME, true)
            apply()
            startActivity(Intent(context, MainActivity::class.java))
            requireActivity().finish()
        }
    }
}