import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.google.protobuf") version "0.8.15"
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.spring") version "1.5.20"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

val grpcVersion = "1.35.0"
val protobufVersion = "3.12.0"
val protocVersion = protobufVersion
val armeriaVersion = "1.4.0"

dependencies {
    implementation("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation("io.grpc:grpc-stub:${grpcVersion}")

    implementation("com.linecorp.armeria:armeria-grpc:${armeriaVersion}")
    implementation("com.linecorp.armeria:armeria-spring-boot2-starter:${armeriaVersion}")

    implementation("org.hibernate.validator:hibernate-validator")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    runtimeOnly("com.linecorp.armeria:armeria-spring-boot2-actuator-starter:${armeriaVersion}")

    // micrometer
    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-statsd")

    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets{
    main {
        java {
            srcDirs("$projectDir/src/main/java/grpc",
                "$projectDir/src/main/java/java","$projectDir/src/main/kotlin")
        }
    }
}

protobuf{
    protoc{
        artifact = "com.google.protobuf:protoc:3.11.0"
    }
    plugins{
        id("grpc"){
            artifact = "io.grpc:protoc-gen-grpc-java:1.26.0"
        }
    }
    generateProtoTasks{
        ofSourceSet("main").forEach{
            it.plugins{
                id("grpc")
            }
        }
    }
    generatedFilesBaseDir = "$projectDir/src"
}
