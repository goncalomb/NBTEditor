import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.hidetake.groovy.ssh.core.Remote
import org.hidetake.groovy.ssh.core.RunHandler
import org.hidetake.groovy.ssh.core.Service
import org.hidetake.groovy.ssh.session.SessionHandler

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1" // Generates plugin.yml
    id("org.hidetake.ssh") version "2.10.1"
    id("com.goncalomb.bukkit.java-conventions")
    id("java")
}

dependencies {
    implementation(project(":adapter_api"))
    implementation(project(":adapter_v1_16_R3"))
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

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xmaxwarns")
    options.compilerArgs.add("10000")
    options.compilerArgs.add("-Xlint:deprecation")
}

val basicssh = remotes.create("basicssh") {
    host = "admin-eu.playmonumenta.com"
    port = 8822
    user = "epic"
    knownHosts = allowAnyHosts
    agent = System.getenv("IDENTITY_FILE") == null
    identity = if (System.getenv("IDENTITY_FILE") == null) null else file(System.getenv("IDENTITY_FILE"))
}

val adminssh = remotes.create("adminssh") {
    host = "admin-eu.playmonumenta.com"
    port = 9922
    user = "epic"
    knownHosts = allowAnyHosts
    agent = System.getenv("IDENTITY_FILE") == null
    identity = if (System.getenv("IDENTITY_FILE") == null) null else file(System.getenv("IDENTITY_FILE"))
}

// Relocation / shading
tasks {
    shadowJar {
       relocate("org.bstats", "com.goncalomb.bukkit.bstats")
    }
}

tasks.create("dev1-deploy") {
    val shadowJar by tasks.named<ShadowJar>("shadowJar")
    dependsOn(shadowJar)
    doLast {
        ssh.runSessions {
            session(basicssh) {
                execute("cd /home/epic/dev1_shard_plugins && rm -f nbteditor*.jar")
                put(shadowJar.archiveFile.get().getAsFile(), "/home/epic/dev1_shard_plugins")
            }
        }
    }
}

tasks.create("dev2-deploy") {
    val shadowJar by tasks.named<ShadowJar>("shadowJar")
    dependsOn(shadowJar)
    doLast {
        ssh.runSessions {
            session(basicssh) {
                execute("cd /home/epic/dev2_shard_plugins && rm -f nbteditor*.jar")
                put(shadowJar.archiveFile.get().getAsFile(), "/home/epic/dev2_shard_plugins")
            }
        }
    }
}

tasks.create("dev3-deploy") {
    val shadowJar by tasks.named<ShadowJar>("shadowJar")
    dependsOn(shadowJar)
    doLast {
        ssh.runSessions {
            session(basicssh) {
                execute("cd /home/epic/dev3_shard_plugins && rm -f nbteditor*.jar")
                put(shadowJar.archiveFile.get().getAsFile(), "/home/epic/dev3_shard_plugins")
            }
        }
    }
}

tasks.create("dev4-deploy") {
    val shadowJar by tasks.named<ShadowJar>("shadowJar")
    dependsOn(shadowJar)
    doLast {
        ssh.runSessions {
            session(basicssh) {
                execute("cd /home/epic/dev4_shard_plugins && rm -f nbteditor*.jar")
                put(shadowJar.archiveFile.get().getAsFile(), "/home/epic/dev4_shard_plugins")
            }
        }
    }
}

tasks.create("futurama-deploy") {
    val shadowJar by tasks.named<ShadowJar>("shadowJar")
    dependsOn(shadowJar)
    doLast {
        ssh.runSessions {
            session(basicssh) {
                execute("cd /home/epic/futurama_shard_plugins && rm -f nbteditor*.jar")
                put(shadowJar.archiveFile.get().getAsFile(), "/home/epic/futurama_shard_plugins")
            }
        }
    }
}

tasks.create("mobs-deploy") {
    val shadowJar by tasks.named<ShadowJar>("shadowJar")
    dependsOn(shadowJar)
    doLast {
        ssh.runSessions {
            session(basicssh) {
                execute("cd /home/epic/mob_shard_plugins && rm -f nbteditor*.jar")
                put(shadowJar.archiveFile.get().getAsFile(), "/home/epic/mob_shard_plugins")
            }
        }
    }
}

tasks.create("stage-deploy") {
    val shadowJar by tasks.named<ShadowJar>("shadowJar")
    dependsOn(shadowJar)
    doLast {
        ssh.runSessions {
            session(basicssh) {
                put(shadowJar.archiveFile.get().getAsFile(), "/home/epic/stage/m12/server_config/plugins")
                execute("cd /home/epic/stage/m12/server_config/plugins && rm -f nbteditor.jar && ln -s " + shadowJar.archiveFileName.get() + " nbteditor.jar")
            }
        }
    }
}

tasks.create("build-deploy") {
    val shadowJar by tasks.named<ShadowJar>("shadowJar")
    dependsOn(shadowJar)
    doLast {
        ssh.runSessions {
            session(adminssh) {
                put(shadowJar.archiveFile.get().getAsFile(), "/home/epic/project_epic/server_config/plugins")
                execute("cd /home/epic/project_epic/server_config/plugins && rm -f nbteditor.jar && ln -s " + shadowJar.archiveFileName.get() + " nbteditor.jar")
                execute("cd /home/epic/project_epic/mobs/plugins && rm -f nbteditor.jar && ln -s ../../server_config/plugins/nbteditor.jar")
            }
        }
    }
}

tasks.create("play-deploy") {
    val shadowJar by tasks.named<ShadowJar>("shadowJar")
    dependsOn(shadowJar)
    doLast {
        ssh.runSessions {
            session(adminssh) {
                put(shadowJar.archiveFile.get().getAsFile(), "/home/epic/play/m8/server_config/plugins")
                put(shadowJar.archiveFile.get().getAsFile(), "/home/epic/play/m11/server_config/plugins")
                execute("cd /home/epic/play/m8/server_config/plugins && rm -f nbteditor.jar && ln -s " + shadowJar.archiveFileName.get() + " nbteditor.jar")
                execute("cd /home/epic/play/m11/server_config/plugins && rm -f nbteditor.jar && ln -s " + shadowJar.archiveFileName.get() + " nbteditor.jar")
            }
        }
    }
}

fun Service.runSessions(action: RunHandler.() -> Unit) =
    run(delegateClosureOf(action))

fun RunHandler.session(vararg remotes: Remote, action: SessionHandler.() -> Unit) =
    session(*remotes, delegateClosureOf(action))

fun SessionHandler.put(from: Any, into: Any) =
    put(hashMapOf("from" to from, "into" to into))
