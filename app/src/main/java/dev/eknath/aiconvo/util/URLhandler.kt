package dev.eknath.aiconvo.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.handleUrlClick(url: String) {
    openBrowser(this, url)
}

private fun openBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    context.startActivity(intent)
}