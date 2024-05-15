plugins {
    java
    id("maven-publish")
    id("co.uzzu.dotenv.gradle") version "4.0.0"
}

group = "ca.kaxx"
version = "0.0.1"

java{
    sourceCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks.jar {
    archiveFileName.set("kaxx-board-" + project.version + ".jar")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/kaxxteam/kaxx-board")
            name = "GitHubPackages"
            credentials {
                username = env.GITHUB_USERNAME.value
                password = env.GITHUB_TOKEN.value
            }
        }
    }
}

