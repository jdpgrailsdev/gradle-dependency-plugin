package org.gradle.plugins.dependency.analyze

import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedDependency

class DependencyAnalysisReport {

	private Set<ResolvedDependency> unusedDeclaredDependencies

	private Set<ResolvedArtifact> usedUndeclaredArtifacts

	DependencyAnalysisReport(Set<ResolvedArtifact> usedUndeclaredArtifacts, Set<ResolvedDependency> unusedDeclaredDependencies) {
		this.unusedDeclaredDependencies = unusedDeclaredDependencies ?: Collections.emptySet()
		this.usedUndeclaredArtifacts = usedUndeclaredArtifacts ?: Collections.emptySet()
	}

	Set<String> getUnusedDeclaredDependencies() {
		unusedDeclaredDependencies.collect { dependency ->
			"${dependency.getModuleGroup()}:${dependency.getModuleName()}:${dependency.getModuleVersion()}:${dependency.getConfiguration()}"
		} as Set<String>
	}

	Set<String> getUsedUndeclaredArtifacts() {
		usedUndeclaredArtifacts.collect { artifact ->
			"${artifact.getModuleVersion().getId().group}:${artifact.getModuleVersion().getId().name}:${artifact.getModuleVersion().getId().version}:compile"
		} as Set<String>
	}
}
