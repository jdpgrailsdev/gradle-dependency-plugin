package org.gradle.plugins.dependency.update

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.TaskAction

class UpdateVersionsTask extends DefaultTask {

	/**
	 * Updates all dependencies that match the provided group ID to their latest released version.
	 * The {@code includeMajorReleases} flag can be used to control if major releases should be
	 * considered as part of the update.
	 *
	 * @param groupId The group ID of the dependencies that should be considered for updating in the
	 * 	project's build script.
	 * @param includeMajorReleases Flag that is used to determine if major version number changes
	 * 	should be included in the possible update candidates.  Defaults to {@code false}.
	 */
	@TaskAction
	public void updateDependencies() {
		def groupId = project.properties.groupId
		def includeMajorReleases = project.properties.includeMajorReleases ?: false

		def dependencies = project.configurations.collectMany { it.allDependencies }.findAll { it.group == groupId}
		dependencies?.each { Dependency dep ->
			def resolvedConfiguration = project.configurations.detachedConfiguration(project.dependencies.create("${dep.group}:${dep.name}:latest.release")).getResolvedConfiguration()
			def resolvedVersionString = resolvedConfiguration.getFirstLevelModuleDependencies().find { it.getModuleName() == dep.name }?.getModuleVersion()
			if(resolvedVersionString) {
				project.logger.debug("Found version ${resolvedVersionString} for dependency ${dep.group}:${dep.name}.")
				DependencyVersion latestVersion = new DependencyVersion(resolvedVersionString)
				DependencyVersion currentVersion = new DependencyVersion(dep.version)

				if(latestVersion.isNewerThan(currentVersion, includeMajorReleases)) {
					// TODO look for each .gradle file
					project.ant.replaceregexp(file: project.file('build.gradle'), match: "${dep.group}:${dep.name}:${dep.version}", replace: "${dep.group}:${dep.name}:${resolvedVersionString}")
					// TODO search sub-modules
				}
			}
		}
	}
}
