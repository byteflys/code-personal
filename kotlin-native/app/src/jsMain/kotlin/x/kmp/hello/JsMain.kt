import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposableInBody

suspend fun main() {
    println("kotlin-js")
    for (i in 1..3) {
        delay(500)
        window.alert("alert $i")
        renderComposableInBody {
            Text("Hello World")
        }
    }
}