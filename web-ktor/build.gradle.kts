plugins {
  application
  kotlin("jvm")
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  compile(project(":common"))
  compile("org.slf4j:slf4j-api:1.7.21")
  compile("org.slf4j:slf4j-simple:1.7.21")
  compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
  compile("io.ktor:ktor-server-core:1.1.2")
  compile("io.ktor:ktor-server-jetty:1.1.2")
  compile("io.ktor:ktor-jackson:1.1.2")
}
