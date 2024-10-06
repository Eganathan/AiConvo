package dev.eknath.aiconvo.presentation.presentation.helpers

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import android.content.Intent.createChooser

fun Context.shareNote(title: String, content: String) {
    val sendIntent: Intent = Intent().apply {
        action = ACTION_SEND
        putExtra(EXTRA_TEXT, "${title}\n${content}")
        type = "text/plain"
    }
    val shareIntent = createChooser(sendIntent, "Share the message:")
    this.startActivity(shareIntent)
}