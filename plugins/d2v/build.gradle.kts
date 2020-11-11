import org.jetbrains.registerDokkaArtifactPublication

dependencies {
    testImplementation("org.jsoup:jsoup:1.12.1")
    testImplementation(project(":plugins:base"))
    testImplementation(project(":plugins:base:base-test-utils"))
    testImplementation(project(":core:content-matcher-test-utils"))
    testImplementation(kotlin("test-junit"))
    testImplementation(project(":kotlin-analysis"))
}

registerDokkaArtifactPublication("d2vPlugin") {
    artifactId = "d2v-plugin"
}
