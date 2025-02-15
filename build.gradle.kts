buildscript {
	repositories {
		mavenCentral()
	}
}

plugins {
	id("org.jetbrains.intellij") version "1.14.1"
	kotlin("jvm") version "1.8.21"
	java
}

repositories {
	mavenCentral()
}

dependencies {
	classpath("lib")
	
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

fun DependencyHandlerScope.classpath(path: String) {
	compileOnly(fileTree("include" to "**/*.jar", "dir" to path))
}

group = "ir.mmd.intellijDev"
version = "4.5.0"

sourceSets["main"].java.srcDirs("src/main/gen")

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

intellij {
	plugins.add("com.intellij.java")
	plugins.add("org.jetbrains.kotlin")
	
	type.set("IC")
	version.set("2023.1.2")
}

kotlin {
	jvmToolchain(17)
}

tasks {
	compileKotlin {
		compilerOptions {
			freeCompilerArgs.add("-Xjvm-default=all")
			freeCompilerArgs.add("-Xcontext-receivers")
		}
	}
	
	val createOpenApiSourceJar by registering(Jar::class) {
		from(sourceSets.main.get().java) {
			include("**/*.java")
		}
		
		from(kotlin.sourceSets.main.get().kotlin) {
			include("**/*.kt")
		}
		
		destinationDirectory.set(layout.buildDirectory.dir("libs"))
		archiveClassifier.set("src")
	}
	
	buildPlugin {
		dependsOn(createOpenApiSourceJar)
		
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
		
		from(createOpenApiSourceJar) {
			into("lib/src")
		}
		
		from("docs/site") {
			into("docs")
		}
	}
	
	runIde {
		autoReloadPlugins.set(true)
	}
	
	prepareSandbox {
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
		from("docs/site") {
			into("${pluginName.get()}/docs")
		}
	}
	
	buildSearchableOptions {
		enabled = environment["SKIP_BSO"] == null
	}
	
	patchPluginXml {
		version.set(project.version.toString())
		sinceBuild.set("231")
		untilBuild.set("233.*")
		changeNotes.set("""
			<h2>Meet the new Feature: Advanced Search! (since v4.5)</h2>
			<ul>
				<li><b>New Feature</b>: #96 Advanced Search</li>
				<li><b>New Feature</b>: advanced search support for java (beta)</li>
				<li><b>Bug Fix</b>: fixed advanced search documentation provider</li>
			</ul>
			<div>
				To install nightly builds use <b>Download And Install Nightly Build</b> Action
				<br>
				To see all new features documentation please check out the <a href='https://mohammadmd1383.github.io/Actionable/Actions%20Documetation/'>Actions Documentation</a> section.
				<br>
				<b>To see the offline docs right from your ide use the action: Open Offline Help</b>
			</div>
		""".trimIndent())
	}
	
	test {
		useJUnitPlatform()
	}
	
	publishPlugin {
		token.set(System.getenv("ACTIONABLE_PUBLISH_TOKEN"))
	}
}
