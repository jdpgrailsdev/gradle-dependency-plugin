package org.gradle.plugins.dependency.analyze

import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedDependency
import org.gradle.api.artifacts.ResolvedModuleVersion

class ScopedResolvedArtifact implements ResolvedArtifact {

    private ResolvedArtifact delegate

    private String scope

    public ScopedResolvedArtifact(ResolvedArtifact artifact, String configurationName) {
        delegate = artifact
        scope = configurationName
    }

    @Override
    public String getClassifier() {
        delegate?.getClassifier() ?: null
    }

    @Override
    public String getExtension() {
        delegate?.getExtension() ?: null
    }

    @Override
    public File getFile() {
        delegate?.getFile() ?: null
    }

    @Override
    public ResolvedModuleVersion getModuleVersion() {
        delegate?.getModuleVersion() ?: null
    }

    @Override
    public String getName() {
        delegate?.getName() ?: null
    }

    @Override
    public ResolvedDependency getResolvedDependency() {
        delegate?.getResolvedDependency() ?: null
    }

    public String getScope() {
        scope
    }

    @Override
    public String getType() {
        delegate?.getType() ?: null
    }
}