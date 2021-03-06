package org.gradle.plugins.dependency.analyze

import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedModuleVersion
import org.gradle.api.artifacts.component.ComponentArtifactIdentifier

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

    public String getScope() {
        scope
    }

    @Override
    public String getType() {
        delegate?.getType() ?: null
    }

    @Override
    public ComponentArtifactIdentifier getId() {
        delegate?.getId() ?: null
    }
}