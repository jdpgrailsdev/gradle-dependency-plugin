package org.gradle.plugins.dependency.analyze

import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedDependency
import org.gradle.api.artifacts.ResolvedModuleVersion

class ScopedResolvedDependency implements ResolvedDependency {

    private ResolvedDependency delegate

    private String scope

    public ScopedResolvedDependency(ResolvedDependency dependency, String configurationName) {
        delegate = dependency
        scope = configurationName
    }
    @Override
    public Set<ResolvedArtifact> getAllArtifacts(ResolvedDependency dependency) {
        delegate?.getAllArtifacts(dependency) ?: [] as Set<ResolvedArtifact>
    }
    @Override
    public Set<ResolvedArtifact> getAllModuleArtifacts() {
        delegate?.getAllModuleArtifacts() ?: [] as Set<ResolvedDependency>
    }
    @Override
    public Set<ResolvedArtifact> getArtifacts(ResolvedDependency dependency) {
        delegate?.getArtifacts(dependency) ?: null
    }
    @Override
    public Set<ResolvedDependency> getChildren() {
        delegate?.getChildren() ?: [] as Set<ResolvedDependency>
    }
    @Override
    public String getConfiguration() {
        delegate?.getConfiguration() ?: null
    }
    @Override
    public ResolvedModuleVersion getModule() {
        delegate?.getModule() ?: null
    }
    @Override
    public Set<ResolvedArtifact> getModuleArtifacts() {
        delegate?.getModuleArtifacts() ?: [] as Set<ResolvedArtifact>
    }
    @Override
    public String getModuleGroup() {
        delegate?.getModuleGroup() ?: null
    }
    @Override
    public String getModuleName() {
        delegate?.getModuleName() ?: null
    }
    @Override
    public String getModuleVersion() {
        delegate?.getModuleVersion() ?: null
    }
    @Override
    public String getName() {
        delegate?.getName() ?: null
    }
    @Override
    public Set<ResolvedArtifact> getParentArtifacts(ResolvedDependency dependency) {
        delegate?.getParentArtifacts(dependency) ?: [] as Set<ResolvedArtifact>
    }
    @Override
    public Set<ResolvedDependency> getParents() {
        delegate?.getParents() ?: [] as Set<ResolvedArtifact>
    }

    public String getScope() {
        scope
    }
}
