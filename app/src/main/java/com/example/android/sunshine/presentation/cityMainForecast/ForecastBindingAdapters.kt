package com.example.android.sunshine.presentation.cityMainForecast

import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.example.android.sunshine.utilities.SunshineWeatherUtils
import com.squareup.picasso.Picasso

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@BindingAdapter("app:setWeatherIcon")
fun setWeatherIcon(view: ImageView, iconPath: String?){
    val realIconName = SunshineWeatherUtils.getIconResourceFromIconPath(iconPath)
    val imageId = view.context.resources.getIdentifier(realIconName, "drawable", view.context.packageName)
    val imageDrawable = view.context.resources.getDrawable(imageId, null)
    view.setImageDrawable(imageDrawable)
}