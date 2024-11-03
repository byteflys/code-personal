package x.kmp.hello

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import platformName

@Composable
@Preview
fun SimpleComposeView() {
    MaterialTheme {
        Text(platformName())
    }
}