import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  base
  kotlin("jvm") version "1.3.21" apply false
}

allprojects {
  group = "io.mfj"
  version = "1.0-SNAPSHOT"

  tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
  }
}
