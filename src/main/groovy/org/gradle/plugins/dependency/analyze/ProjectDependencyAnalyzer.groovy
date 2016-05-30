package org.gradle.plugins.dependency.analyze

import groovy.io.FileType

import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedConfiguration
import org.gradle.api.artifacts.ResolvedDependency
import org.gradle.api.artifacts.ResolvedModuleVersion
import org.gradle.plugins.dependency.analyze.asm.visitor.DependencyClassVisitor
import org.gradle.plugins.dependency.analyze.util.ClassExtractor

/**
 * Adapted from org.apache.maven.shared.dependency.analyzer.ClassFileVisitorUtils
 *
 */
class ProjectDependencyAnalyzer {

    private ClassExtractor classExtractor = new ClassExtractor()

    private DependencyClassVisitor dependencyClassVisitor = new DependencyClassVisitor()

    DependencyAnalysisReport analyze(Project project) {
        Set<ResolvedArtifact> resolvedArtifacts = getResolvedArtifacts(project)

        Map<ResolvedArtifact, Set<String>> dependencyClassesMap = buildDependencyClassesMap(resolvedArtifacts)

        Set<String> dependencyClassesFromCompiledSource = findDependenciesFromCompiledSource(project.buildDir)
        project.getLogger().debug("Dependencies from compiled source: ${dependencyClassesFromCompiledSource}")

        //TODO just get declared artifacts from build, not all resolved artifacts
        Set<ScopedResolvedDependency> declaredDependencies = getProjectDeclaredDependencies(project)

        Set<ScopedResolvedArtifact> usedArtifacts = findUsedArtifacts(dependencyClassesMap, dependencyClassesFromCompiledSource)

        Set<ScopedResolvedDependency> unusedDeclaredDependencies = declaredDependencies.findAll { declaredDependency -> !usedArtifacts.find { usedArtifact -> compareDependencyToArtifact(declaredDependency, usedArtifact) } } ?: Collections.emptySet()

        Set<ScopedResolvedArtifact> usedUndeclaredArtifacts = usedArtifacts.findAll { usedArtifact -> !declaredDependencies.find { declaredDependency -> compareDependencyToArtifact(declaredDependency, usedArtifact) } } ?: Collections.emptySet()

        new DependencyAnalysisReport(usedUndeclaredArtifacts, unusedDeclaredDependencies)
    }

    private Set<ScopedResolvedArtifact> getResolvedArtifacts(Project project) {
        Set<ScopedResolvedArtifact> resolvedArtifacts = new HashSet<ResolvedArtifact>()

        project.getConfigurations().getNames().each { configurationName ->
            ResolvedConfiguration resolvedConfiguration = project.getConfigurations().getByName(configurationName).resolvedConfiguration
            resolvedConfiguration.resolvedArtifacts.collect(resolvedArtifacts) { ResolvedArtifact artifact ->
                new ScopedResolvedArtifact(artifact, configurationName)
            }
        }

        project.getLogger().debug("Resolved artifacts = ${resolvedArtifacts.collect { it.getId()?.getDisplayName() } }")
        resolvedArtifacts
    }

    private Set<ScopedResolvedDependency> getProjectDeclaredDependencies(Project project) {
        Set<ScopedResolvedDependency> declaredDependencies = new HashSet<ScopedResolvedDependency>()

        project.getConfigurations().getNames().each { configurationName ->
            ResolvedConfiguration resolvedConfiguration = project.getConfigurations().getByName(configurationName).resolvedConfiguration
            resolvedConfiguration.firstLevelModuleDependencies.collect(declaredDependencies) { ResolvedDependency dependency ->
                new ScopedResolvedDependency(dependency, configurationName)
            }
        }

        project.getLogger().debug("Project declared dependencies = ${declaredDependencies.collect { "${it.getModuleGroup()}:${it.getModuleName()}:${it.getModuleVersion()}" } }")
        declaredDependencies
    }

    /**
     * Creates a map of each dependency/artifact to the list of compiled class files contained in said dependency/artifact.
     * This map is then used to cross reference against the dependency classes referenced in the compiled source of
     * the project being analyzed for matches to determine required dependencies for the project.
     * @param resolvedArtifacts The set of resolved artifacts for the current project.
     * @return A map of resolved artifacts to the set of class names for each compiled class found in the
     * 	dependency/artifact or an empty map if there are no resolved artifacts to process.
     */
    private Map<ScopedResolvedArtifact, Set<String>> buildDependencyClassesMap(Set<ScopedResolvedArtifact> resolvedArtifacts) {
        Map<ScopedResolvedArtifact, Set<String>> dependencyClassMap = [:]
        resolvedArtifacts.each { ScopedResolvedArtifact artifact ->
            dependencyClassMap.put(artifact, classExtractor.extractClasses(artifact))
        }
        dependencyClassMap
    }

    /**
     * Returns the set of class files referenced by source files found in this project.  This
     * set is then used to determine which declared dependencies are necessary and which ones
     * are not necessary for compilation, execution, test, etc of this project.
     * @param buildDirectory The build directory of the current project that may contain {@code .class} files.
     * @return The set of dependency classes extracted from the compiled source for the project or
     * 	an empty set if no dependency classes are found.
     */
    private Set<String> findDependenciesFromCompiledSource(File buildDirectory) {
        Set<String> projectClasses = new HashSet<String>()
        buildDirectory.eachFileRecurse(FileType.FILES) { File file ->
            if(file.absolutePath ==~ /.*\.class/ ) {
                dependencyClassVisitor.visitClass(file)
                projectClasses.addAll(dependencyClassVisitor.getDependencyClasses())
            }
        }
        projectClasses
    }

    private Set<ScopedResolvedArtifact> findUsedArtifacts(Map<ScopedResolvedArtifact, Set<String>> dependencyClassesMap, Set<String> dependencyClassesFromCompiledSource) {
        Set<ScopedResolvedArtifact> usedArtifacts = new HashSet<ScopedResolvedArtifact>()

        dependencyClassesFromCompiledSource.each { className ->
            ResolvedArtifact usedArtifact = dependencyClassesMap.find { key, value -> value.contains(className) }?.key
            if(usedArtifact) {
                usedArtifacts << usedArtifact
            }
        }

        usedArtifacts
    }

    private boolean compareDependencyToArtifact(ScopedResolvedDependency dependency, ScopedResolvedArtifact artifact) {
        ResolvedModuleVersion artifactModule = artifact.moduleVersion
        dependency.getModuleGroup() == artifactModule.getId().getGroup() && dependency.getModuleName() == artifactModule.getId().getName() && dependency.getModuleVersion() == artifactModule.getId().getVersion()
    }
}
