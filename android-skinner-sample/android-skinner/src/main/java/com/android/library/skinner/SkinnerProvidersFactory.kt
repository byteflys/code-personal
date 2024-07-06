package com.android.library.skinner

import com.android.library.skinner.provider.BasicAttributeSkinner

object SkinnerProvidersFactory {

    private val providers = mutableListOf<SkinnerProvider>(BasicAttributeSkinner)

    fun providers() = providers

    fun registerViewProvider(provider: SkinnerProvider) = apply {
        if (!providers.contains(provider))
            providers.add(provider)
    }
}