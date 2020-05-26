package com.example.android.sunshine.utilities

import org.mockito.Mockito

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)