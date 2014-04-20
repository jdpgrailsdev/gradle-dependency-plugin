package org.gradle.plugins.dependency.analyze.asm.visitor

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

class ASMAnnotationVisitor extends AnnotationVisitor {

	private ASMVisitorResult result

	ASMAnnotationVisitor(ASMVisitorResult result) {
		super(Opcodes.ASM5)
		this.result = result ?: new ASMVisitorResult()
	}

	@Override
	void visit(String name, Object value) {
		if (value instanceof Type) {
			result.addType(value)
		}
	}

	@Override
	void visitEnum(String name, String descriptor, String value) {
		result.addDescriptor(descriptor)
	}

	@Override
	AnnotationVisitor visitAnnotation(String name, String descriptor) {
		result.addDescriptor(descriptor);
		this
	}

	@Override
	AnnotationVisitor visitArray(String name) {
		this
	}
}