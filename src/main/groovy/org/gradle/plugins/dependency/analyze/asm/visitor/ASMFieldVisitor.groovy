package org.gradle.plugins.dependency.analyze.asm.visitor

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.Opcodes

class ASMFieldVisitor extends FieldVisitor {

	private AnnotationVisitor annotationVisitor
	private ASMVisitorResult result

	ASMFieldVisitor(AnnotationVisitor annotationVisitor, ASMVisitorResult result) {
		super(Opcodes.ASM5)
		this.result = result ?: new ASMVisitorResult()
		this.annotationVisitor = annotationVisitor ?: new ASMAnnotationVisitor(this.result)
	}

	@Override
	AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		result.addDescriptor(descriptor)
		annotationVisitor
	}
}