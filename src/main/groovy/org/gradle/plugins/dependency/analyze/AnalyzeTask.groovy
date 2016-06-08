package org.gradle.plugins.dependency.analyze

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.internal.tasks.options.Option
import org.gradle.api.tasks.diagnostics.AbstractReportTask
import org.gradle.api.tasks.diagnostics.internal.ReportRenderer
import org.gradle.plugins.dependency.analyze.internal.AsciiDependencyAnalysisReportRenderer

class AnalyzeTask extends AbstractReportTask {

    private AsciiDependencyAnalysisReportRenderer renderer = new AsciiDependencyAnalysisReportRenderer()

    private ProjectDependencyAnalyzer analyzer = new ProjectDependencyAnalyzer()

    private Set<Configuration> configurations

    @Override
    protected void generate(Project project) throws IOException {
        renderer.render(analyzer.analyze(project, getReportConfigurations()))
    }

    @Override
    protected ReportRenderer getRenderer() {
        renderer
    }

    @Option(option = "configuration", description = "The configuration to generate the report for.")
    public void setConfiguration(String configurationName) {
        this.configurations = Collections.singleton(getTaskConfigurations().getByName(configurationName))
    }

    private Set<Configuration> getReportConfigurations() {
        return configurations != null ? configurations : getTaskConfigurations().findAll { it.name == 'compile' }
    }

    private ConfigurationContainer getTaskConfigurations() {
        return getProject().getConfigurations()
    }
}
