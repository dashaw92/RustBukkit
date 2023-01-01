plugins {
    `java-library`
}

group = "me.danny"
version = "0.0.1-SNAPSHOT"
val spigotVersion = "1.19.2-R0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven ( url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/" )
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:$spigotVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
}