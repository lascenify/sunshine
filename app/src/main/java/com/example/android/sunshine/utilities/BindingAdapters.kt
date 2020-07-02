package com.example.android.sunshine.utilities

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.BindingAdapter

@BindingAdapter("app:visibility")
fun setVisibilty(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.visibility = VISIBLE
    } else {
        view.visibility = GONE
    }
}