/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.sunshine.R
import com.example.android.sunshine.framework.SunshineApplication
import com.example.android.sunshine.framework.di.component.CityForecastComponent
import javax.inject.Provider

typealias ForecastComponentProvider = Provider<CityForecastComponent>
class MainActivity : AppCompatActivity(), ForecastComponentProvider{

    lateinit var forecastComp: CityForecastComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        forecastComp = (applicationContext as SunshineApplication).appComponent.cityForecastComp().create()
    }

    override fun get(): CityForecastComponent = forecastComp


}