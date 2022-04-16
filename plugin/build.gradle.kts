import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.goncalomb.bukkit.java-conventions")
    id("java")
}

dependencies {
    implementation("org.bstats:bstats-bukkit:1.5")
    compileOnly("org.spigotmc:spigot-api:1.16.3-R0.1-SNAPSHOT")

    testImplementation("junit:junit:4.13.1")
    testImplementation("org.spigotmc:spigot-api:1.16.3-R0.1-SNAPSHOT")
}

group = "com.goncalomb.bukkit"
description = "NBTEditor"
version = rootProject.version

// Relocation / shading
tasks {
    shadowJar {
       relocate("org.bstats", "com.goncalomb.bukkit.bstats")
    }
}
