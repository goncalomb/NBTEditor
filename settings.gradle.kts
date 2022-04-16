rootProject.name = "nbteditor"
include(":NBTEditor")
project(":NBTEditor").projectDir = file("plugin")

pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://papermc.io/repo/repository/maven-public/")
  }
}
