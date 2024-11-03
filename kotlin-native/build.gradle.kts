import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins.withType<YarnPlugin> {
    extensions.configure<YarnRootExtension> {
        yarnLockMismatchReport = YarnLockMismatchReport.NONE
    }
}