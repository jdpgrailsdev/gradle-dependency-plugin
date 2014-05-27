package org.gradle.plugins.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.plugins.dependency.analyze.AnalyzeTask
import org.gradle.plugins.dependency.update.UpdateVersionsTask

class DependencyPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.logger?.info("Applying Gradle dependency plugin to ${project.name}...")

		// Create the analyze dependency task and register it with the project.
		project.tasks.create(name: 'dependency-analyze', type: AnalyzeTask)
		Task analyzeTask = project.tasks.getByName('dependency-analyze')
		analyzeTask.setDependsOn(['classes'])
		analyzeTask.setDescription('Analyzes the dependencies of this project and determines which are: used and declared; used and undeclared; unused and declared')
		analyzeTask.setGroup('Help')

		// Create the update versions task and register it with the project.
		project.tasks.create(name: 'dependency-version-update', type: UpdateVersionsTask)
		Task updateTask = project.tasks.getByName('dependency-version-update')
		updateTask.setDescription('Updates any matching dependencies to the latest release version.')
		updateTask.setGroup('Help')
	}
}