package com.android.library.skinner.provider

import android.util.AttributeSet
import android.view.View
import com.android.library.skinner.SkinnerProvider
import com.android.library.skinner.SkinnerValues.ATTRIBUTE_PROVIDER
import com.android.library.skinner.SkinnerValues.NAMESPACE_SKIN

abstract class BaseSkinnerProvider : SkinnerProvider {

    fun getProviderNames(view: View, attrs: AttributeSet): String? {
        return attrs.getAttributeValue(NAMESPACE_SKIN, ATTRIBUTE_PROVIDER)
    }

    fun getAllProviders(view: View, attrs: AttributeSet): List<String> {
        val providerNames = getProviderNames(view, attrs)
        providerNames ?: return emptyList()
        val providers = providerNames.replace(" ", "").split("|")
        return providers
    }
}