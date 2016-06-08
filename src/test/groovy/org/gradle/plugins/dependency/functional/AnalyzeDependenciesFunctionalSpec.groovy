package org.gradle.plugins.dependency.functional

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class AnalyzeDependenciesFunctionalSpec extends AbstractFunctionalSpec {

    def "test that when a project has declared all required dependencies, no unused or undeclared dependencies are listed"() {
        setup:
            buildFile << """
                    plugins {
                        id 'dependency'
                        id 'java'
                    }

                    repositories {
                        mavenLocal()
                        mavenCentral()
                    }

                    dependencies {
                        compile 'org.apache.commons:commons-lang3:3.4'
                    }
                """

            def sourceFile = new File(getClass().getResource('/sourceWithMatchingDependencies/HelloWorld.java').toURI())
            def destFile = new File(testProjectDir.getRoot(), 'src/main/java/com/test/HelloWorld.java')
            destFile.parentFile.mkdirs()
            destFile << sourceFile.bytes
        when:
            GradleRunner runner = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments('analyzeDependencies', '--stacktrace', '--refresh-dependencies')
                .withDebug(true)
                .withGradleDistribution(new URI(getGradleDistribution()))
                .withPluginClasspath(pluginClasspath)
             BuildResult result =  runner.build()
        then:
            result.task(':analyzeDependencies').getOutcome() == TaskOutcome.SUCCESS
            result.getOutput().contains('Used undeclared dependencies found:') == false
            result.getOutput().contains('Unused declared dependencies found:') == false
    }

    def "test that when a project has a depedency on an undeclared dependency, the undeclared dependency is listed"() {
        setup:
            buildFile << """
                    plugins {
                        id 'dependency'
                        id 'java'
                    }

                    repositories {
                        mavenLocal()
                        mavenCentral()
                    }

                    dependencies {
                        compile 'org.apache.httpcomponents:httpclient:4.5.2'
                    }
                """

            def sourceFile = new File(getClass().getResource('/sourceWithUndeclaredDependencies/HelloWorld.java').toURI())
            def destFile = new File(testProjectDir.getRoot(), 'src/main/java/com/test/HelloWorld.java')
            destFile.parentFile.mkdirs()
            destFile << sourceFile.bytes
        when:
            GradleRunner runner = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments('analyzeDependencies', '--stacktrace', '--refresh-dependencies')
                .withDebug(true)
                .withGradleDistribution(new URI(getGradleDistribution()))
                .withPluginClasspath(pluginClasspath)
             BuildResult result =  runner.build()
        then:
            result.task(':analyzeDependencies').getOutcome() == TaskOutcome.SUCCESS
            result.getOutput().contains('Used undeclared dependencies found:') == true
            result.getOutput().contains('commons-codec:commons-codec:1.9') == true
            result.getOutput().contains('Unused declared dependencies found:') == false
    }

    def "test that when a project has an unused depedency declared, the unused dependency is listed"() {
        setup:
            buildFile << """
                    plugins {
                        id 'dependency'
                        id 'java'
                    }

                    repositories {
                        mavenLocal()
                        mavenCentral()
                    }

                    dependencies {
                        compile 'org.apache.commons:commons-lang3:3.4'
                        compile 'org.apache.httpcomponents:httpclient:4.5.2'
                    }
                """

            def sourceFile = new File(getClass().getResource('/sourceWithMatchingDependencies/HelloWorld.java').toURI())
            def destFile = new File(testProjectDir.getRoot(), 'src/main/java/com/test/HelloWorld.java')
            destFile.parentFile.mkdirs()
            destFile << sourceFile.bytes
        when:
            GradleRunner runner = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments('analyzeDependencies', '--stacktrace', '--refresh-dependencies', '--configuration=compile')
                .withDebug(true)
                .withGradleDistribution(new URI(getGradleDistribution()))
                .withPluginClasspath(pluginClasspath)
             BuildResult result =  runner.build()
        then:
            result.task(':analyzeDependencies').getOutcome() == TaskOutcome.SUCCESS
            result.getOutput().contains('Used undeclared dependencies found:') == false
            result.getOutput().contains('Unused declared dependencies found:') == true
            result.getOutput().contains('org.apache.httpcomponents:httpclient') == true
    }
}