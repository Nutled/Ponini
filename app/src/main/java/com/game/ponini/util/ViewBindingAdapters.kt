package com.game.ponini.util

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Created by Viнt@rь on 23.04.2020
 */
@BindingAdapter("visible")
fun visible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}