package org.gradle.plugins.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.plugins.dependency.analyze.AnalyzeTask

class DependencyPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.logger?.info("Applying Gradle dependency plugin to ${project.name}...")

        // Create the analyze dependency task and register it with the project.
        project.tasks.create(name: 'analyzeDependencies', type: AnalyzeTask)
        Task analyzeTask = project.tasks.getByName('analyzeDependencies')
        analyzeTask.setDependsOn(['classes'])
        analyzeTask.setDescription('Analyzes the dependencies of this project and determines which are: used and declared; used and undeclared; unused and declared')
        analyzeTask.setGroup('Help')
    }
}