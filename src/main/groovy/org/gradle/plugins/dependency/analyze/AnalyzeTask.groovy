package org.gradle.plugins.dependency.analyze

import org.gradle.api.Project
import org.gradle.api.tasks.diagnostics.AbstractReportTask
import org.gradle.api.tasks.diagnostics.internal.ReportRenderer
import org.gradle.plugins.dependency.analyze.internal.AsciiDependencyAnalysisReportRenderer

class AnalyzeTask extends AbstractReportTask {

    AsciiDependencyAnalysisReportRenderer renderer = new AsciiDependencyAnalysisReportRenderer()

    ProjectDependencyAnalyzer analyzer = new ProjectDependencyAnalyzer()

    @Override
    protected void generate(Project project) throws IOException {
        renderer.render(analyzer.analyze(project))
    }

    @Override
    protected ReportRenderer getRenderer() {
        renderer
    }
}
