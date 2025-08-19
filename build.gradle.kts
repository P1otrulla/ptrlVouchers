import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    `java-library`
    id("com.gradleup.shadow") version "9.0.2"
    id("de.eldoria.plugin-yml.paper") version "0.7.1"
}

group = "dev.piotrulla"
version = "1.0.0"
val mainPackage = "$group.vouchers"

repositories {
    gradlePluginPortal()
    maven("https://maven-central.storage-download.googleapis.com/maven2")

    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.panda-lang.org/releases")
    maven("https://repo.eternalcode.pl/releases")
    maven("https://maven.enginehub.org/repo")
    maven("https://storehouse.okaeri.eu/repository/maven-releases/")
    maven("https://repo.eternalcode.pl/snapshots/")
    maven("https://repo.eternalcode.pl/releases/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")

    paperLibrary("net.kyori:adventure-platform-bukkit:4.4.1")
    paperLibrary("net.kyori:adventure-text-minimessage:4.24.0")

    paperLibrary("com.eternalcode:multification-okaeri:1.1.4")
    paperLibrary("com.eternalcode:multification-bukkit:1.1.4")
    paperLibrary("com.eternalcode:eternalcode-commons-bukkit:1.3.0")
    paperLibrary("com.eternalcode:eternalcode-commons-adventure:1.3.0")

    paperLibrary("dev.rollczi:litecommands-bukkit:3.10.4")
    paperLibrary("dev.rollczi:litecommands-adventure:3.10.4")

    paperLibrary("eu.okaeri:okaeri-configs-yaml-snakeyaml:5.0.2")
    paperLibrary("eu.okaeri:okaeri-configs-serdes-commons:5.0.2")
    paperLibrary("eu.okaeri:okaeri-configs-serdes-bukkit:5.0.2")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")

    implementation("org.bstats:bstats-bukkit:3.0.0")
}

paper {
    name = "ptrlVouchers"
    version = "${project.version}"
    prefix = "ptrlVouchers"
    author = "Piotrulla"

    main = "dev.piotrulla.vouchers.VoucherPlugin"
    bootstrapper = "dev.piotrulla.vouchers.VoucherBootstrap"
    loader = "dev.piotrulla.vouchers.VoucherLoader"

    generateLibrariesJson = true
    apiVersion = "1.19"


    serverDependencies {
        register("Vault") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = true
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.compileJava {
    options.compilerArgs = listOf("-Xlint:deprecation", "-parameters")
    options.encoding = "UTF-8"
    options.release = 21
}

tasks.shadowJar {
    archiveFileName.set("ptrlVouchers v${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
    )

    // bstas relocation
    relocate("org.bstats", "dev.piotrulla.ptrlvouchers.libs.org.bstats")
}
