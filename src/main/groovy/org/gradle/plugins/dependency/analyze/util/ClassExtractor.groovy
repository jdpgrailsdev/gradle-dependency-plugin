package org.gradle.plugins.dependency.analyze.util;

import groovy.io.FileType

import java.util.jar.JarEntry
import java.util.jar.JarInputStream

import org.gradle.api.artifacts.ResolvedArtifact

public class ClassExtractor {

	Set<String> extractClasses(ResolvedArtifact resolvedArtifact) {
		if(resolvedArtifact.file && resolvedArtifact.file.exists()) {
			extractClassesFromFile(resolvedArtifact.file)
		} else {
			[] as Set
		}
	}

	private Set<String> extractClassesFromFile(File artifactFile) {
		if(artifactFile.name.endsWith('.jar')) {
			_extractClassesFromFile(artifactFile)
		} else if(artifactFile.isDirectory()) {
			_extractDirectory(artifactFile)
		}
	}

	private Set<String> _extractClassesFromFile(File artifactFile) {
		Set<String> classSet = [] as Set<String>
		JarInputStream is = new JarInputStream(artifactFile.newInputStream())
		JarEntry jarEntry = null

		while((jarEntry = is.getNextJarEntry()) != null) {
			if(jarEntry.name.endsWith('.class')) {
				classSet << jarEntry.name.substring( 0, jarEntry.name.length() - 6 ).replace( '/', '.' )
			}
		}

		classSet
	}

	private Set<String> _extractDirectory(File artifactDirectory) {
		Set<String> classSet = [] as Set<String>
		artifactDirectory.eachFileMatch FileType.FILES, ~/.*\.class/, { File match ->
			classSet.addAll(_extractClassesFromFile(match))
		}
		classSet
	}
}