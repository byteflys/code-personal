package com.android.code.commons

import android.content.ContentResolver
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File

object UriEx {

    const val SCHEME_HTTP = "http"
    const val SCHEME_HTTPS = "https"
    const val FILE_URI = "file:///"
    const val FILE_ASSET_URI = "file:///android_asset/"

    fun isWebsiteUri(uri: String?): Boolean {
        if (uri.isNullOrEmpty()) {
            return false
        }
        return uri.startsWith(SCHEME_HTTP, true) || uri.startsWith(SCHEME_HTTPS, true)
    }

    fun isFileUri(uri: String?): Boolean {
        if (uri.isNullOrEmpty()) {
            return false
        }
        return uri.startsWith(FILE_URI, true)
    }

    fun String?.isWebsiteOrFile(): Boolean {
        return isWebsiteUri(this) || isFileUri(this)
    }

    fun assetFileUri(fileName: String): Uri {
        return Uri.parse("$FILE_ASSET_URI$fileName")
    }

    fun Uri.getExtensionName(): String {
        val context = Global.application
        val typeMap = MimeTypeMap.getSingleton()
        when (scheme) {
            ContentResolver.SCHEME_FILE -> {
                val url = Uri.fromFile(File(path)).toString()
                return MimeTypeMap.getFileExtensionFromUrl(url)
            }
            ContentResolver.SCHEME_CONTENT -> {
                val type = context.contentResolver.getType(this)
                return typeMap.getExtensionFromMimeType(type).orEmpty()
            }
            ContentResolver.SCHEME_ANDROID_RESOURCE -> {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(context, this)
                val type = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
                return typeMap.getExtensionFromMimeType(type).orEmpty()
            }
            SCHEME_HTTP,
            SCHEME_HTTPS -> return ""
            else -> return ""
        }
    }

    fun Uri.getMimeType(): String {
        return MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(getExtensionName())
            .orEmpty()
    }
}