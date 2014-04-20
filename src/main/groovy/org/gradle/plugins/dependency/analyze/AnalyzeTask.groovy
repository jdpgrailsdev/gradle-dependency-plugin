package org.gradle.plugins.dependency.analyze

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AnalyzeTask extends DefaultTask {

	ProjectDependencyAnalyzer analyzer = new ProjectDependencyAnalyzer()

	@TaskAction
	public void analyze() {
		analyzer.analyze(project)
	}
}
