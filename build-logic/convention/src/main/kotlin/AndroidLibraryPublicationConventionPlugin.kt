import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.credentials.HttpHeaderCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.authentication.http.HttpHeaderAuthentication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

class AndroidLibraryPublicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("maven-publish")
            group = GROUP_ID
            version = publicationVersion
            extensions.configure<LibraryExtension> {
                publishing {
                    singleVariant(PUBLICATION_VARIANT) {
                        withSourcesJar()
                        withJavadocJar()
                    }
                }
            }
            extensions.configure<PublishingExtension> {
                publications {
                    register<MavenPublication>("release") {
                        groupId = GROUP_ID
                        artifactId = target.name
                        version = publicationVersion
                        afterEvaluate {
                            from(components[PUBLICATION_VARIANT])
                        }
                    }
                }
                repositories {
                    maven {
                        name = "GitLab"
                        setUrl("https://gitlab.com/api/v4/projects/67598015/packages/maven")
                        credentials(HttpHeaderCredentials::class.java) {
                            name = "Job-Token"
                            value = System.getenv("CI_JOB_TOKEN")
                        }
                        authentication {
                            create<HttpHeaderAuthentication>("header")
                        }
                    }
                }
            }
        }
    }

    private companion object {
        const val GROUP_ID = "com.edurda77.mooncompass"
        const val PUBLICATION_VARIANT = "release"
        val Project.publicationVersion: String
            get() = findProperty("com.edurda77.mooncompass.version").toString()
    }
}
