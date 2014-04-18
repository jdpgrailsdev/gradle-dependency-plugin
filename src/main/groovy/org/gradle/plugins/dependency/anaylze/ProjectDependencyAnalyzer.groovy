package org.gradle.plugins.dependency.anaylze

import groovy.io.FileType

import java.util.jar.JarEntry
import java.util.jar.JarInputStream

import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedConfiguration

/**
 * Adapted from org.apache.maven.shared.dependency.analyzer.ClassFileVisitorUtils
 * @author ddcjpearlin
 *
 */
class ProjectDependencyAnalyzer {

	def analyze(Project project) {
		ResolvedConfiguration resolvedConfiguration = project.configurations.getByName('all').getResolvedConfiguration()
		buildClassesMap(resolvedConfiguration.resolvedArtifacts)

	}

	private Map<ResolvedArtifact, Set<String>> buildClassesMap(Set<ResolvedArtifact> resolvedArtifacts) {
		resolvedArtifacts.collectEntries { ResolvedArtifact artifact ->
			if(artifact.file && artifact.file.exists()) {
				[artifact : extractClasses(artifact.file)]
			}
		}
	}

	private Set<String> extractClasses(File artifactFile) {
		if(artifactFile.name.endsWith('.jar')) {
			_extractClasses(artifactFile)
		} else if(artifactFile.isDirectory()) {
			_extractDirectory(artifactFile)
		}
	}

	private Set<String> _extractClasses(File artifactFile) {
		Set<String> classSet = [] as Set<String>
		JarInputStream is = new JarInputStream(artifactFile.newInputStream())
		JarEntry jarEntry = null

		while((jarEntry = is.getNextJarEntry()) != null) {
			if(jarEntry.name.endsWith('.class')) {
				classSet << jarEntry.name.substring( 0, jarEntry.name.length() - 6 ).replace( '/', '.' )
			}
		}

		classSet
	}

	private Set<String> _extractDirectory(File artifactDirectory) {
		Set<String> classSet = [] as Set<String>
		artifactDirectory.eachFileMatch FileType.FILES, ~/.*\.class/, { File match ->
			classSet.addAll(_extractClasses(match))
		}
		classSet
	}
}
