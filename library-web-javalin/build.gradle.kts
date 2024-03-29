plugins {
  application
  kotlin("jvm")
}

application {
  mainClassName = "io.mfj.kotlinnight.library.web.javalin.JavalinApp"
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  compile(project(":library-common"))
  compile("org.slf4j:slf4j-api:1.7.21")
  compile("org.slf4j:slf4j-simple:1.7.21")
  compile("io.javalin:javalin:2.6.0")
  compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
}
