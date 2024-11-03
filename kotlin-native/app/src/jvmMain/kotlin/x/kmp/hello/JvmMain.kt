import androidx.compose.material.Text
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    println(platformName())
    application {
        Window(::exitApplication) {
            Text("Kotlin/JVM")
        }
    }
}