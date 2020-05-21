package com.example.android.sunshine.presentation.common

import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.example.android.sunshine.R
import com.example.android.sunshine.utilities.SunshineWeatherUtils

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@BindingAdapter("app:setWeatherIcon")
fun setWeatherIcon(view: ImageView, iconPath: String?){
    val realIconName = SunshineWeatherUtils.getIconResourceFromIconPath(iconPath)
    val imageId = view.context.resources.getIdentifier(realIconName, "drawable", view.context.packageName)
    val imageDrawable = view.context.resources.getDrawable(imageId, null)
    view.setImageDrawable(imageDrawable)
}

@BindingAdapter("app:setVisibility")
fun setVisibility(view: View, isVisible: Boolean){
    if (isVisible)
        view.visibility = View.VISIBLE
    else
        view.visibility = View.GONE
}


@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("app:setBackgroundColor")
fun setBackgroundColor(view: View, isSelected: Boolean){
    val cardView = view as CardView
    val context = view.context
    val theme = context.theme
    if (isSelected)
        cardView.backgroundTintList = context.resources.getColorStateList(R.color.colorSelected, theme)
    else
        cardView.backgroundTintList = context.resources.getColorStateList(R.color.colorPrimaryDark, theme)

}