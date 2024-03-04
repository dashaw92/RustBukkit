plugins {
    `java-library`
}

group = "me.danny"
version = "0.0.1-SNAPSHOT"
val spigotVersion = "1.20.4-R0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:$spigotVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType(JavaCompile::class.java).forEach {
    it.options.compilerArgs.add("--enable-preview")
}