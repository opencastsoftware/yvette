plugins {
    `java-library`
    alias(libs.plugins.gradleJavaConventions)
}

repositories { mavenCentral() }

group = "com.opencastsoftware"

description = "A diagnostic reporting library for Java"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
    registerFeature("graphicalReports") { usingSourceSet(sourceSets["main"]) }
}

dependencies {
    implementation(libs.apacheCommonsLang)
    implementation(libs.apacheCommonsText)
    "graphicalReportsApi"(libs.jansi)
    testImplementation(libs.junitJupiter)
    testImplementation(libs.hamcrest)
    testImplementation(libs.jqwik)
    testImplementation(libs.equalsVerifier)
    testImplementation(libs.toStringVerifier)
}

mavenPublishing {
    coordinates("com.opencastsoftware", "yvette", project.version.toString())

    pom {
        name.set("yvette")
        description.set(project.description)
        url.set("https://github.com/opencastsoftware/yvette")
        inceptionYear.set("2023")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        organization {
            name.set("Opencast Software Europe Ltd")
            url.set("https://opencastsoftware.com")
        }
        developers {
            developer {
                id.set("DavidGregory084")
                name.set("David Gregory")
                organization.set("Opencast Software Europe Ltd")
                organizationUrl.set("https://opencastsoftware.com/")
                timezone.set("Europe/London")
                url.set("https://github.com/DavidGregory084")
            }
        }
        ciManagement {
            system.set("Github Actions")
            url.set("https://github.com/opencastsoftware/yvette/actions")
        }
        issueManagement {
            system.set("GitHub")
            url.set("https://github.com/opencastsoftware/yvette/issues")
        }
        scm {
            connection.set("scm:git:https://github.com/opencastsoftware/yvette.git")
            developerConnection.set("scm:git:git@github.com:opencastsoftware/yvette.git")
            url.set("https://github.com/opencastsoftware/yvette")
        }
    }
}

tasks.withType<JavaCompile>() {
    // Target Java 8
    options.release.set(8)
}

tasks.named<Test>("test") { useJUnitPlatform { includeEngines("junit-jupiter", "jqwik") } }
