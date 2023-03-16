plugins {
    `java-library`
    `maven-publish`
    `signing`
    jacoco
    id("com.github.ben-manes.versions") version "0.46.0"
    id("io.github.gradle-nexus.publish-plugin") version "1.2.0"
    id("me.qoomon.git-versioning") version "5.2.0"
}

repositories {
    mavenCentral()
}

group = "com.opencastsoftware"
description = "A diagnostic reporting library for Java"

version = "0.0.0-SNAPSHOT"

gitVersioning.apply {
    refs {
        branch(".+") {
            describeTagPattern = "v(?<version>.*)".toPattern()
            version = "\${describe.tag.version:-0.0.0}-\${describe.distance}-\${commit.short}-SNAPSHOT"
        }
        tag("v(?<version>.*)") {
            version = "\${ref.version}"
        }
    }
    rev {
        describeTagPattern = "v(?<version>.*)".toPattern()
        version = "\${describe.tag.version:-0.0.0}-\${describe.distance}-\${commit.short}-SNAPSHOT"
    }
}

extra["isReleaseVersion"] = !version.toString().endsWith("SNAPSHOT")

java {
    withJavadocJar()
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))

    registerFeature("graphicalReports") {
        usingSourceSet(sourceSets["main"])
    }
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-text:1.10.0")
    "graphicalReportsImplementation"("org.fusesource.jansi:jansi:2.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("net.jqwik:jqwik:1.7.2")
    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.14.1")
    testImplementation("com.jparams:to-string-verifier:1.4.8")
}

publishing {
    publications {
        create<MavenPublication>("libMaven") {
            from(components["java"])

            pom {
                name.set("yvette")
                description.set(project.description)
                url.set("https://github.com/opencastsoftware/yvette")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("DavidGregory084")
                        name.set("David Gregory")
                        email.set("david.gregory@opencastsoftware.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/opencastsoftware/yvette.git")
                    developerConnection.set("scm:git:git@github.com:opencastsoftware/yvette.git")
                    url.set("https://github.com/opencastsoftware/yvette")
                }
            }
        }
    }
}

signing {
    setRequired({ project.extra["isReleaseVersion"] as Boolean })
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["libMaven"])
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        includeEngines("junit-jupiter", "jqwik")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}