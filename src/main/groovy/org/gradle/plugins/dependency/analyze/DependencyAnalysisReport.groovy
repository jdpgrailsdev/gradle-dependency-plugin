package org.gradle.plugins.dependency.analyze


class DependencyAnalysisReport {

    private Set<ScopedResolvedDependency> unusedDeclaredDependencies

    private Set<ScopedResolvedArtifact> usedUndeclaredArtifacts

    DependencyAnalysisReport(Set<ScopedResolvedArtifact> usedUndeclaredArtifacts, Set<ScopedResolvedDependency> unusedDeclaredDependencies) {
        this.unusedDeclaredDependencies = unusedDeclaredDependencies ?: Collections.emptySet()
        this.usedUndeclaredArtifacts = usedUndeclaredArtifacts ?: Collections.emptySet()
    }

    Set<String> getUnusedDeclaredDependencies() {
        unusedDeclaredDependencies.collect { ScopedResolvedDependency dependency ->
            "${dependency.getModuleGroup()}:${dependency.getModuleName()}:${dependency.getModuleVersion()}:${dependency.getScope()}"
        } as Set<String>
    }

    Set<String> getUsedUndeclaredArtifacts() {
        usedUndeclaredArtifacts.collect { ScopedResolvedArtifact artifact ->
            "${artifact.getModuleVersion().getId().group}:${artifact.getModuleVersion().getId().name}:${artifact.getModuleVersion().getId().version}:${artifact.getScope()}"
        } as Set<String>
    }
}
