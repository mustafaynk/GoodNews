package com.ynk.goodnews.extensions

import android.R
import android.app.Activity
import android.graphics.PorterDuff
import android.os.Build
import android.view.Menu
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes


fun Activity.setSystemBarColor(@ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(color)
    }
}

fun Activity.setSystemBarLight() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = this.findViewById<View>(R.id.content)
        var flags = view.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        view.systemUiVisibility = flags
    }
}

fun Activity.changeMenuIconColor(menu: Menu, @ColorInt color: Int) {
    for (i in 0 until menu.size()) {
        val drawable = menu.getItem(i).icon ?: continue
        drawable.mutate()
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}