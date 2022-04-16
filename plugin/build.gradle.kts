import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1" // Generates plugin.yml
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

// Configure plugin.yml generation
bukkit {
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    main = "com.goncalomb.bukkit.nbteditor.NBTEditor"
    apiVersion = "1.13"
    name = "NBTEditor"
    authors = listOf("goncalomb", "Combustible")
    depend = listOf()
    softDepend = listOf()
}

// Relocation / shading
tasks {
    shadowJar {
       relocate("org.bstats", "com.goncalomb.bukkit.bstats")
    }
}
