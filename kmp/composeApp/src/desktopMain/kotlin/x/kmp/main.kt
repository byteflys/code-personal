package x.kmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        title = "kmp",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}