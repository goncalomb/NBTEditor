plugins {
    id("com.goncalomb.bukkit.java-conventions")
}

dependencies {
    implementation(project(":adapter_api"))
    compileOnly("com.destroystokyo.paper:paper:1.16.5-R0.1-SNAPSHOT")
}

description = "adapter_v1_16_R3"
version = rootProject.version
