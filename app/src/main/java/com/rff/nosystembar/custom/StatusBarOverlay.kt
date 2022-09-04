package com.rff.nosystembar.custom

import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup

class StatusBarOverlay(context: Context?) : ViewGroup(context) {
        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
        override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
            println("customViewGroup::"+ "**********Intercepted")
            return true
        }
    }