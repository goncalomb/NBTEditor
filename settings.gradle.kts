rootProject.name = "nbteditor"
include(":adapter_api")
include(":adapter_v1_16_R3")
include(":NBTEditor")
project(":NBTEditor").projectDir = file("plugin")

pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://papermc.io/repo/repository/maven-public/")
  }
}
