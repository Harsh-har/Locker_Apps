package com.example.LockerApp.Classes

import android.graphics.drawable.Drawable

class AppLogoClass(
    val appname: String,
    val appIcon: Drawable,
    val packageName: String,
    var isSelected: Boolean = false,
    var isLocked: Boolean = false)

