package x.kmp.hello

import platform.UIKit.UIViewController
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        SimpleComposeView()
    }
}