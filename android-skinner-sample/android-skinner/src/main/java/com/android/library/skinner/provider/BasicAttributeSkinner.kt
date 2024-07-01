package com.android.library.skinner.provider

import android.util.AttributeSet
import android.view.View
import com.android.library.skinner.SkinnerKit.hasSkinnableAttribute
import com.android.library.skinner.SkinnerValues.ATTRIBUTE_BACKGROUND
import com.android.library.skinner.SkinnerValues.ATTRIBUTE_SRC
import com.android.library.skinner.SkinnerValues.ATTRIBUTE_TEXT_COLOR
import com.android.library.skinner.SkinnerValues.NAMESPACE_ANDROID

object BasicAttributeSkinner : BaseSkinnerProvider() {

    override fun isProviderSupported(view: View, attrs: AttributeSet): Boolean {
        val providers = getAllProviders(view, attrs)
        if (providers.isEmpty()) {
            return true
        }
        if (providers.contains(BasicAttributeSkinner::class.simpleName)) {
            return true
        }
        return false
    }

    override fun hookView(view: View, attrs: AttributeSet) {
        if (hasSkinnableAttribute(view, attrs, NAMESPACE_ANDROID, ATTRIBUTE_BACKGROUND)) {
            hookBackgroundAttribute(view, attrs)
        }
        if (hasSkinnableAttribute(view, attrs, NAMESPACE_ANDROID, ATTRIBUTE_TEXT_COLOR)) {
            hookTextColorAttribute(view, attrs)
        }
        if (hasSkinnableAttribute(view, attrs, NAMESPACE_ANDROID, ATTRIBUTE_SRC)) {
            hookSrcAttribute(view, attrs)
        }
    }

    private fun hookBackgroundAttribute(view: View, attrs: AttributeSet) {

    }

    private fun hookTextColorAttribute(view: View, attrs: AttributeSet) {

    }

    private fun hookSrcAttribute(view: View, attrs: AttributeSet) {

    }
}