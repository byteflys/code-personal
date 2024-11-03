import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.renderComposableInBody

suspend fun main() {
    console.log(platformName())
    for (i in 1..3) {
        delay(500)
        window.alert("${platformName()} $i")
        renderComposableInBody {
            Div()
            Img("https://th.bing.com/th/id/OIP.IKtTTdtHQYe30BCCkZvv1AHaHa")
            Div()
        }
    }
}