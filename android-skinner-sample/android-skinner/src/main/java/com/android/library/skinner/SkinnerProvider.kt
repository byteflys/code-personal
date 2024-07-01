package com.android.library.skinner

import android.util.AttributeSet
import android.view.View

interface SkinnerProvider {

    fun isProviderSupported(view: View, attrs: AttributeSet): Boolean

    fun hookView(view: View, attrs: AttributeSet)
}