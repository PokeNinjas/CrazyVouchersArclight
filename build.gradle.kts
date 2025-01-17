plugins {
    `java-library`

    `maven-publish`

    id("com.modrinth.minotaur") version "2.6.0"

    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    /**
     * Placeholders
     */
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    /**
     * NBT API
     */
    maven("https://repo.codemc.org/repository/maven-public/")

    /**
     * Paper Team
     */
//    maven("https://repo.papermc.io/repository/maven-public/")

    // Spigot
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    // Item NBT api
    maven("https://repo.codemc.org/repository/maven-public/")

    /**
     * Everything else we need.
     */
    mavenCentral()
}

dependencies {
    implementation("de.tr7zw", "item-nbt-api-plugin", "2.11.1")

    implementation("org.bstats", "bstats-bukkit", "3.0.0")

//    compileOnly("io.papermc.paper", "paper-api", "${project.extra["minecraft_version"]}-R0.1-SNAPSHOT")

    compileOnly("org.spigotmc", "spigot-api", "1.19.3-R0.1-SNAPSHOT")

    compileOnly("me.clip", "placeholderapi", "2.11.2") {
        exclude(group = "org.spigotmc")
        exclude(group = "org.bukkit")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(project.extra["java_version"].toString()))
}

val isBeta: Boolean = extra["isBeta"].toString().toBoolean()

fun getPluginVersion(): String {
    return if (isBeta) "${project.version}-BETA" else project.version.toString()
}

fun getPluginVersionType(): String {
    return if (isBeta) "beta" else "release"
}

tasks {
    shadowJar {
        archiveFileName.set("${rootProject.name}-${getPluginVersion()}.jar")

        listOf(
            "de.tr7zw",
            "org.bstats"
        ).forEach {
            relocate(it, "${project.group}.plugin.lib.$it")
        }
    }

    modrinth {
        token.set(System.getenv("MODRINTH_TOKEN"))
        projectId.set(rootProject.name.toLowerCase())

        versionName.set("${rootProject.name} ${getPluginVersion()}")
        versionNumber.set(getPluginVersion())

        versionType.set(getPluginVersionType())

        uploadFile.set(shadowJar.get())

        autoAddDependsOn.set(true)

        gameVersions.addAll(listOf("1.18", "1.18.1", "1.18.2", "1.19", "1.19.1", "1.19.2", "1.19.3"))
        loaders.addAll(listOf("paper", "purpur"))

        //<h3>The first release for CrazyVouchers on Modrinth! 🎉🎉🎉🎉🎉<h3><br> If we want a header.
        changelog.set("""
                <h4>Changes:</h4>
                 <p>Added 1.18.2 support.</p>
                <h4>Bug Fixes:</h4>
                 <p>N/A</p>
            """.trimIndent())
    }

    compileJava {
        options.release.set(project.extra["java_version"].toString().toInt())
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(
                "name" to rootProject.name,
                "group" to project.group,
                "version" to getPluginVersion(),
                "description" to project.description,
                "website" to "https://modrinth.com/plugin/${rootProject.name.toLowerCase()}"
            )
        }
    }
}

publishing {
    val mavenExt: String = if (isBeta) "beta" else "releases"

    repositories {
        maven("https://repo.crazycrew.us/$mavenExt") {
            name = "crazycrew"
            //credentials(PasswordCredentials::class)
            credentials {
                username = System.getenv("REPOSITORY_USERNAME")
                password = System.getenv("REPOSITORY_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = "${project.group}"
            artifactId = rootProject.name.toLowerCase()
            version = getPluginVersion()
            from(components["java"])
        }
    }
}