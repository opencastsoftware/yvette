# yvette

[![CI](https://github.com/opencastsoftware/yvette/actions/workflows/ci.yml/badge.svg)](https://github.com/opencastsoftware/yvette/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/opencastsoftware/yvette/branch/main/graph/badge.svg?token=JHVF151VM1)](https://codecov.io/gh/opencastsoftware/yvette)
[![Maven Central](https://img.shields.io/maven-central/v/com.opencastsoftware/yvette)](https://search.maven.org/search?q=g%3Acom.opencastsoftware+AND+a%3Ayvette)
[![javadoc](https://javadoc.io/badge2/com.opencastsoftware/yvette/javadoc.svg)](https://javadoc.io/doc/com.opencastsoftware/yvette)
[![License](https://img.shields.io/github/license/opencastsoftware/yvette?color=blue)](https://spdx.org/licenses/Apache-2.0.html)

A diagnostic reporting library for Java, ported from the Rust library [miette](https://github.com/zkat/miette).

## Installation

Gradle (build.gradle / build.gradle.kts):
```groovy
implementation("com.opencastsoftware:yvette:0.1.0")
```

Maven (pom.xml):
```xml
<dependency>
    <groupId>com.opencastsoftware</groupId>
    <artifactId>yvette</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Usage

## Acknowlegements

This project wouldn't exist without the work of [zkat](https://github.com/zkat) and the other miette contributors.

## License

All code in this repository is licensed under the Apache License, Version 2.0. See [LICENSE](./LICENSE).