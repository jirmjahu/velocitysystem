plugins {
    id("java")
}

group = "net.jirmjahu.velocitysystem"
version = "1.0.0"

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    implementation ("com.moandjiezana.toml:toml4j:0.7.2")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
}