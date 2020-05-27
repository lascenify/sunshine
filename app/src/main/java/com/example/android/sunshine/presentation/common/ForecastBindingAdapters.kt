package com.example.android.sunshine.presentation.common

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.example.android.sunshine.R
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.framework.SunshinePreferences
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.utilities.ChartUtilities
import com.example.android.sunshine.utilities.WeatherUtils
import com.github.mikephil.charting.charts.LineChart

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@BindingAdapter("app:setWeatherIcon")
fun setWeatherIcon(view: ImageView, iconPath: String?){
    val realIconName = WeatherUtils.getIconResourceFromIconPath(iconPath)
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

@BindingAdapter("app:setTemperature")
fun setTemperature(view: View, temperature: Double){
    view as TextView
    val formattedTemperature = WeatherUtils.formatTemperature(view.context, temperature)
    view.text = formattedTemperature
}

@BindingAdapter("app:setSimpleTemperature")
fun setSimpleTemperature(view: View, temperature: Int){
    view as TextView
    val formattedTemperature = WeatherUtils.formatSimpleTemperature(view.context, temperature)
    view.text = formattedTemperature
}


@BindingAdapter("app:setChartData")
fun setChartData (view: LineChart, list: List<ForecastListItem>?){
    if (list != null) {
        val isMetric = SunshinePreferences.isMetric(view.context)
        ChartUtilities.setUpChart(list, view, "Temperatures", isMetric)
    }
}
