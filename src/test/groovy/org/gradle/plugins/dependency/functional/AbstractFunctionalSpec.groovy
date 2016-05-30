package org.gradle.plugins.dependency.functional

import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class AbstractFunctionalSpec extends Specification {

    @Rule
    TemporaryFolder testProjectDir = new TemporaryFolder()

    File buildFile

    File propertiesFile

    List pluginClasspath

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        propertiesFile = testProjectDir.newFile('gradle.properties')
        propertiesFile << """group = com.test.project
version = 1.0.0-SNAPSHOT
jdk.version = 1.8
description = Test Project"""
        pluginClasspath = getClass().classLoader.findResource('plugin-classpath.txt').readLines().collect { new File(it) }.findAll { !it.name.contains('xercesImpl') }
    }

    def cleanup() {
        buildFile.deleteOnExit()
        propertiesFile.deleteOnExit()
    }

    String getGradleDistribution() {
        Properties properties = new Properties()
        properties.load(new File('gradle/wrapper/gradle-wrapper.properties').newInputStream())
        properties.get('distributionUrl')
    }
}
