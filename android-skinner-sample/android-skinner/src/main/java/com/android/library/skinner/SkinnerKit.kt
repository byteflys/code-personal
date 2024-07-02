package com.android.library.skinner

import android.util.AttributeSet
import android.view.View
import com.android.library.skinner.SkinnerValues.PREFIX_SKINNABLE
import com.android.library.skinner.SkinnerValues.RESOURCE_ID_INVALID

object SkinnerKit {

    fun isResourceAvailable(resourceId: Int) = resourceId != RESOURCE_ID_INVALID

    fun isResourceSkinnable(resourceId: Int): Boolean {
        if (!isResourceAvailable(resourceId)) {
            return false
        }
        val resourceName = SkinnerAssetManager.originResources.getResourceName(resourceId)
        return resourceName.startsWith(PREFIX_SKINNABLE, true)
    }

    fun hasSkinnableAttribute(view: View, attrs: AttributeSet, namespace: String, attribute: String): Boolean {
        val resourceId = getAttributeResourceId(view, attrs, namespace, attribute)
        return isResourceSkinnable(resourceId)
    }

    fun getAttributeResourceId(view: View, attrs: AttributeSet, namespace: String, attribute: String): Int {
        return attrs.getAttributeResourceValue(namespace, attribute, RESOURCE_ID_INVALID)
    }
}