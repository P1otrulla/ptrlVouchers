import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "dev.piotrulla"
version = "1.0.0"
val mainPackage = "$group.vouchers"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()

    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")}
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/")}
    maven { url = uri("https://papermc.io/repo/repository/maven-public/")}
    maven { url = uri("https://repository.minecodes.pl/releases") }
    maven { url = uri("https://repo.panda-lang.org/releases") }
    maven { url = uri("https://repo.eternalcode.pl/releases") }
    maven { url = uri("https://maven.enginehub.org/repo") }
}

dependencies {
    // Engine
    compileOnly("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")

    // Configs
    implementation("net.dzikoysk:cdn:1.14.4") {
        exclude("kotlin")
    }

    // adventure
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")
    implementation("net.kyori:adventure-text-minimessage:4.13.0")

    // command framework
    implementation("dev.rollczi.litecommands:bukkit:2.8.8")

    // Inventory framework
    implementation("dev.triumphteam:triumph-gui:3.1.4")

    // Economy
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
}

bukkit {
    main = "$mainPackage.VoucherPlugin"
    apiVersion = "1.13"
    prefix = "VoucherPlugin"
    author = "Piotrulla"
    name = "VoucherPlugin"
    depend = listOf("Vault")
    version = "${project.version}"
}

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf("-Xlint:deprecation", "-parameters")
    options.javaModuleVersion.set("16")
    options.encoding = "UTF-8"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("VoucherPlugin v${project.version}.jar")

    val prefix = "$mainPackage.libs"

    listOf(
        "panda",
        "org.panda_lang",
        "org.bstats",
        "net.dzikoysk",
        "net.kyori",
        "dev.triumphteam",
        "dev.rollczi",
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }
}