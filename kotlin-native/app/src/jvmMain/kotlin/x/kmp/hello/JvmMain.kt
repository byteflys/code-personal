package x.kmp.hello

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import platformName

fun main() {
    println(platformName())
    application {
        Window(::exitApplication) {
            SimpleComposeView()
        }
    }
}