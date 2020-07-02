package com.example.android.sunshine.utilities

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan


fun spannable(func: () -> SpannableString) = func()

private fun span(s: CharSequence, o: Any) = (
        if (s is String) SpannableString(s) else s as? SpannableString
            ?: SpannableString("")
        ).apply { setSpan(o, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

operator fun SpannableString.plus(s: SpannableString) = SpannableString(TextUtils.concat(this, s))

operator fun SpannableString.plus(s: String) = SpannableString(TextUtils.concat(this, s))

fun bold(s: CharSequence) = span(s, StyleSpan(Typeface.BOLD))

fun italic(s: CharSequence) = span(s, StyleSpan(Typeface.ITALIC))
