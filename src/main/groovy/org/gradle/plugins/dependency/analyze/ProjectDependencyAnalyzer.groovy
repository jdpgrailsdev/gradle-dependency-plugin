package org.gradle.plugins.dependency.analyze

import groovy.io.FileType

import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedConfiguration
import org.gradle.plugins.dependency.anaylze.util.ClassExtractor

/**
 * Adapted from org.apache.maven.shared.dependency.analyzer.ClassFileVisitorUtils
 * @author ddcjpearlin
 *
 */
class ProjectDependencyAnalyzer {

	private ClassExtractor classExtractor = new ClassExtractor()

	def analyze(Project project) {
		ResolvedConfiguration resolvedConfiguration = project.configurations.getByName('all').getResolvedConfiguration()

		Map<ResolvedArtifact, Set<String>> dependencyClassesMap = buildClassesMap(resolvedConfiguration.resolvedArtifacts)

		Set<String> projectClasses = getProjectClasses(project)

		Set<ResolvedArtifact> declaredDependencies = resolvedConfiguration.resolvedArtifacts ?: [] as Set
	}

	private Map<ResolvedArtifact, Set<String>> buildClassesMap(Set<ResolvedArtifact> resolvedArtifacts) {
		resolvedArtifacts.collectEntries { ResolvedArtifact artifact ->
			[artifact : classExtractor.extractClasses(artifact)]
		}
	}

	private Set<String> getProjectClasses(Project project) {
		Set<String> projectClasses = [] as Set<String>
		project.buildDir.eachFileMatch FileType.FILES, ~/.*\.class/, { File match ->
			projectClasses << match.absolutePath
		}
		projectClasses
	}
}
