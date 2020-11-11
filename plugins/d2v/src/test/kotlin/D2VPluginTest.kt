package mathjaxTest

import org.jetbrains.dokka.links.DRI
import org.jetbrains.dokka.mathjax.D2VPlugin
import org.jetbrains.dokka.testApi.testRunner.AbstractCoreTest
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.TestOutputWriterPlugin

class D2VPluginTest : AbstractCoreTest() {

    @Test
    fun bindPlugin() {
        val configuration = dokkaConfiguration {
            sourceSets {
                sourceSet {
                    sourceRoots = listOf("src/main/kotlin/test/Test.kt")
                }
            }
        }
        val source =
                """
            |/src/main/kotlin/test/Test.kt
            |package example
			|
            | /**
            | * A Dumb interface
            | */
            | interface Dumb
			|
            """.trimIndent()
        val writerPlugin = TestOutputWriterPlugin()
        testInline(
                source,
                configuration,
                pluginOverrides = listOf(writerPlugin, D2VPlugin())
        ) {


            documentablesMergingStage = { module ->
                Assertions.assertEquals(1, module.children.size, "Expected just a single package in module")
                Assertions.assertEquals(1, module.packages.size, "Expected just a single package in module")

                val pkg = module.packages.single()
                Assertions.assertEquals("example", pkg.dri.packageName, "Expected 'default' package in module")
                Assertions.assertEquals(1, pkg.children.size, "Expected just a single child in 'default' package")

                val classlike = pkg.classlikes.single()

                Assertions.assertEquals(
                        DRI("example", "Dumb"),
                        classlike.dri
                )
                Assertions.assertEquals(0, classlike.functions.size)
            }

        }
    }

}
