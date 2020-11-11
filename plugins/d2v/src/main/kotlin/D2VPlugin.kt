package  org.jetbrains.dokka.mathjax


import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.model.*
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.transformers.documentation.PreMergeDocumentableTransformer

class D2VPlugin : DokkaPlugin() {

    val defaultJavaMethodFiltering by extending {
        CoreExtensions.preMergeDocumentableTransformer providing ::DefaultJavaMethodeFilterTransformer
    }


}


class DefaultJavaMethodeFilterTransformer(val context: DokkaContext) : PreMergeDocumentableTransformer {

    override fun invoke(modules: List<DModule>): List<DModule> {

        println("Filtering equals, hashCode, toString")
        return modules.map { module ->
            module.copy(
                    packages = module.packages.map { pck ->
                        pck.copy(
                                classlikes = filterDClasslike(pck.classlikes)
                        )
                    }
            )
        }
    }

    /**
     * Remove functions equals, hashCode, toString, ...
     */
    private fun filterDClasslike(classlikes: List<DClasslike>): List<DClasslike> =
            classlikes.map { cl: DClasslike ->
                when (cl) {
                    is DEnum        -> cl.copy()
                    is DObject      -> cl.copy()
                    is DAnnotation  -> cl.copy()
                    is DClass       -> cl.copy(functions = filterFunctions(cl.functions))
                    is DInterface   -> cl.copy(functions = filterFunctions(cl.functions))
                }
            }


    private fun filterFunctions(functions: List<DFunction>): List<DFunction> =
            functions
                .filter { !filteredFunctions.contains(it.name) && !it.name.startsWith("component")}
                .map { it.copy() }

}


private val filteredFunctions = setOf("equals", "hashCode", "toString")


