package com.android.library.skinner.provider

import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.library.skinner.SkinnerResources.resourceType
import com.android.library.skinner.SkinnerResources.skinColor
import com.android.library.skinner.SkinnerResources.skinDrawable
import com.android.library.skinner.SkinnerKit.getAttributeResourceId
import com.android.library.skinner.SkinnerKit.hasSkinnableAttribute
import com.android.library.skinner.SkinnerValues.ATTRIBUTE_BACKGROUND
import com.android.library.skinner.SkinnerValues.ATTRIBUTE_SRC
import com.android.library.skinner.SkinnerValues.ATTRIBUTE_TEXT_COLOR
import com.android.library.skinner.SkinnerValues.NAMESPACE_ANDROID
import com.android.library.skinner.SkinnerValues.RESOURCE_TYPE_COLOR
import com.android.library.skinner.SkinnerValues.RESOURCE_TYPE_DRAWABLE

object BasicAttributeSkinner : BaseSkinnerProvider() {

    override fun isProviderSupported(view: View, attrs: AttributeSet): Boolean {
        val providers = getAllProviders(view, attrs)
        if (providers.isEmpty()) {
            return true
        }
        return super.isProviderSupported(view, attrs)
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
        val resourceId = getAttributeResourceId(view, attrs, NAMESPACE_ANDROID, ATTRIBUTE_BACKGROUND)
        val resourceType = resourceType(resourceId)
        if (resourceType == RESOURCE_TYPE_COLOR) {
            view.setBackground(ColorDrawable(skinColor(resourceId)))
        }
        if (resourceType == RESOURCE_TYPE_DRAWABLE) {
            view.setBackground(skinDrawable(resourceId))
        }
    }

    private fun hookTextColorAttribute(view: View, attrs: AttributeSet) {
        val resourceId = getAttributeResourceId(view, attrs, NAMESPACE_ANDROID, ATTRIBUTE_TEXT_COLOR)
        if (view is TextView) {
            view.setTextColor(skinColor(resourceId))
        }
    }

    private fun hookSrcAttribute(view: View, attrs: AttributeSet) {
        val resourceId = getAttributeResourceId(view, attrs, NAMESPACE_ANDROID, ATTRIBUTE_SRC)
        if (view is ImageView) {
            view.setImageDrawable(skinDrawable(resourceId))
        }
    }
}