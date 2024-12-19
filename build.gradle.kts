// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    //id("org.sonarqube") version "4.4.1.3373"
    id ("org.sonarqube") version "3.5.0.2730"
}

sonarqube {
    properties {
        property("sonar.projectName", "KPITAssignment1")
        property("sonar.projectKey", "com.silverst.kpitassignment")
        property("sonar.host.url", "http://localhost:9000")

        // Authentication details
        //property("sonar.login", "admin")
        //property("sonar.password", "admin1") // Replace with a token if available (preferred for security)
        property("sonar.login", "sqp_429d89b4e224441ad0505cbb733878d084822638")

        // Source and Test directories
        property("sonar.sources", "src/main/") // Specify the main source code folder
        property("sonar.tests", "src/test/,src/androidTest/")  // Specify the test folder

        // Reports and Binaries
        property("sonar.android.lint.report", "build/reports/lint-results.xml")
        property("sonar.java.binaries", "build/intermediates/javac/debug/classes")

        // Exclude specific directories or files (optional)
        property("sonar.exclusions", "**/build/**,**/*.png,**/*.jpg,**/com/silverst/kpitassignment/presentation/**,**/com/silverst/kpitassignment/MainActivity.*")

        // Include Kotlin source files (if using Kotlin)
        property("sonar.kotlin.file.suffixes", ".kt,.kts")
        property("sonar.java.coveragePlugin", "jacoco")

        println("JACOCO PATH FOR REPORT: ${layout.projectDirectory.dir("app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml").asFile}")

        property("sonar.coverage.jacoco.xmlReportPaths", layout.projectDirectory.dir("app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml").asFile.toString())
        //property("sonar.coverage.jacoco.xmlReportPaths", layout.buildDirectory.dir("app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"))
    }
}


tasks.register("jacocoTestReport", JacocoReport::class) {
    dependsOn("testDebugUnitTest") // Ensure unit tests run before generating the report

    reports {
        xml.required.set(true)  // Generate XML report for SonarQube
        html.required.set(true) // Optionally generate an HTML report
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/com/silverst/kpitassignment/presentation/**",
        "**/com/silverst/kpitassignment/MainActivity.*"
    )

    val debugTree = fileTree(layout.buildDirectory.dir("intermediates/javac/debug").get()) {
        exclude(fileFilter)
    }
    val kotlinTree = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug").get()) {
        exclude(fileFilter)
    }
    val mainSrc = "${project.projectDir}/src/main/java"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree, kotlinTree))
    executionData.setFrom(fileTree(layout.buildDirectory.dir("app/build/reports/jacoco/jacocoTestReport").get()) {
        include("testDebugUnitTest.exec")
    })
}
