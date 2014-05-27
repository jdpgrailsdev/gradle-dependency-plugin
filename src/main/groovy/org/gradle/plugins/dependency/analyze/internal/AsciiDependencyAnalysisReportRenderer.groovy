package org.gradle.plugins.dependency.analyze.internal

import org.gradle.api.tasks.diagnostics.internal.TextReportRenderer
import org.gradle.logging.StyledTextOutput
import org.gradle.plugins.dependency.analyze.DependencyAnalysisReport

/**
 * Inspired by org.gradle.api.tasks.diagnostics.internal.dependencies.AsciiDependencyReportRenderer
 *
 *
 */
class AsciiDependencyAnalysisReportRenderer extends TextReportRenderer {

	void render(DependencyAnalysisReport report) {
		if(report.getUsedUndeclaredArtifacts()) {
			getTextOutput().withStyle(StyledTextOutput.Style.Header).text('Used undeclared dependencies found:')
			report.getUsedUndeclaredArtifacts().each {
				getTextOutput().withStyle(StyledTextOutput.Style.Normal).text("\t${it}")
			}
		}

		if(report.getUnusedDeclaredDependencies()) {
			getTextOutput().withStyle(StyledTextOutput.Style.Header).text('Unused declared dependencies found:')
			report.getUnusedDeclaredDependencies().each {
				getTextOutput().withStyle(StyledTextOutput.Style.Normal).text("\t${it}")
			}
		}
	}
}