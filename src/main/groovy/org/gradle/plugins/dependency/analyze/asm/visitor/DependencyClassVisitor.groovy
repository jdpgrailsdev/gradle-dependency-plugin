package org.gradle.plugins.dependency.analyze.asm.visitor

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.signature.SignatureVisitor

class DependencyClassVisitor {

	private ASMVisitorResult result = new ASMVisitorResult()

	void visitClass(String className, File classFile) {
		try {
			ClassReader classReader = new ClassReader(classFile.newInputStream());

			AnnotationVisitor annotationVisitor = new ASMAnnotationVisitor(result);
			SignatureVisitor signatureVisitor = new ASMSignatureVisitor(result);
			FieldVisitor fieldVisitor = new ASMFieldVisitor(annotationVisitor, result)
			MethodVisitor methodVisitor = new ASMMethodVisitor(annotationVisitor, signatureVisitor, result);
			ClassVisitor classVisitor = new ASMClassVisitor(annotationVisitor, fieldVisitor, methodVisitor, signatureVisitor, result);

			classReader.accept(classVisitor, 0)
		} catch (e) {
			// TODO throw Gradle task exception
		}
	}
}
