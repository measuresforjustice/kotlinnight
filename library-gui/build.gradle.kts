plugins {
  application
  kotlin("jvm")
}

application {
  mainClassName = "io.mfj.kotlinnight.library.gui.LibraryApp"
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  compile(project(":library-common"))
  compile("org.slf4j:slf4j-api:1.7.21")
  compile("org.slf4j:slf4j-simple:1.7.21")
  compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
  compile("no.tornado:tornadofx:1.7.18")
  compile("com.github.kittinunf.fuel:fuel:2.0.1")
  compile("com.github.kittinunf.fuel:fuel-jackson:2.0.1")
}

