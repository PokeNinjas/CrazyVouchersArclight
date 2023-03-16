@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("crazyvouchers.paper-plugin")

    alias(settings.plugins.minotaur)
    alias(settings.plugins.run.paper)
}

repositories {
    /**
     * Placeholders
     */
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    // Spigot
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    // Item NBT api
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    implementation("de.tr7zw", "item-nbt-api-plugin", "2.11.1")
    implementation(libs.bstats.bukkit)

//    compileOnly(libs.papermc)
    compileOnly("org.spigotmc", "spigot-api", "1.19.3-R0.1-SNAPSHOT")

    compileOnly(libs.placeholder.api)
}

val github = settings.versions.github.get()
val extension = settings.versions.extension.get()

val beta = settings.versions.beta.get().toBoolean()

val type = if (beta) "beta" else "release"

tasks {
    shadowJar {
        archiveFileName.set("${rootProject.name}+Paper+${rootProject.version}.jar")

        listOf(
            "de.tr7zw.changeme.nbtapi",
            "org.bstats"
        ).forEach { relocate(it, "${rootProject.group}.library.$it") }
    }

    runServer {
        minecraftVersion("1.19.4")
    }

    modrinth {
        token.set(System.getenv("MODRINTH_TOKEN"))
        projectId.set(rootProject.name.lowercase())

        versionName.set("${rootProject.name} ${rootProject.version}")
        versionNumber.set(rootProject.version.toString())

        versionType.set(type)

        uploadFile.set(shadowJar.get())

        autoAddDependsOn.set(true)

        gameVersions.addAll(
            listOf(
                "1.19",
                "1.19.1",
                "1.19.2",
                "1.19.3",
                "1.19.4"
            )
        )

        loaders.addAll(listOf("paper", "purpur"))

        //<h3>The first release for CrazyVouchers on Modrinth! 🎉🎉🎉🎉🎉<h3><br> If we want a header.
        changelog.set(
            """
                <h4>Changes:</h4>
                 <p>Added 1.19.4 support</p>
                 <p>Removed 1.18.2 and below support</p>
                <h4>Under the hood changes</h4>
                 <p>Simplified build script</p>
                <h4>Bug Fixes:</h4>
                 <p>N/A</p>
            """.trimIndent()
        )
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(
                "name" to rootProject.name,
                "group" to rootProject.group,
                "version" to rootProject.version,
                "description" to rootProject.description,
                "website" to "https://modrinth.com/$extension/${rootProject.name.lowercase()}"
            )
        }
    }
}

publishing {
    repositories {
        val repo = if (beta) "beta" else "releases"
        maven("https://repo.crazycrew.us/$repo") {
            name = "crazycrew"
            // Used for locally publishing.
            // credentials(PasswordCredentials::class)

            credentials {
                username = System.getenv("REPOSITORY_USERNAME")
                password = System.getenv("REPOSITORY_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = "${rootProject.name.lowercase()}-api"
            version = rootProject.version.toString()

            from(components["java"])
        }
    }
}