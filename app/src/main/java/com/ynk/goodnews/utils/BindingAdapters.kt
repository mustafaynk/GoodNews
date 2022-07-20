package com.ynk.goodnews.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ynk.goodnews.utils.glidessl.GlideApp
import java.text.SimpleDateFormat
import java.util.*

class BindingAdapters {

    //Image Binding - I didn't write newsviewmodel for just this method
    companion object {
        @BindingAdapter("bind:imgUrl")
        @JvmStatic
        fun setImage(imageView: ImageView, imgUrl: String?) {
            GlideApp.with(imageView.context).load(imgUrl).into(imageView)
        }

        @BindingAdapter("app:textToDate")
        @JvmStatic
        fun setTextFromData(textView: TextView, date: Date?) {
            date?.let {
                textView.text = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                    .format(it)
            }

        }
    }
}