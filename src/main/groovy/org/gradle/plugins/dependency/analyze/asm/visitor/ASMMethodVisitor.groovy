package org.gradle.plugins.dependency.analyze.asm.visitor

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.signature.SignatureReader
import org.objectweb.asm.signature.SignatureVisitor

class ASMMethodVisitor extends MethodVisitor {

	private AnnotationVisitor annotationVisitor
	private SignatureVisitor signatureVisitor
	private ASMVisitorResult result

	ASMMethodVisitor(AnnotationVisitor annotationVisitor, SignatureVisitor signatureVisitor, ASMVisitorResult result) {
		super(Opcodes.ASM5)
		this.result = result ?: new ASMVisitorResult()
		this.annotationVisitor = annotationVisitor ?: new ASMAnnotationVisitor(this.result)
		this.signatureVisitor = signatureVisitor ?: new ASMSignatureVisitor(this.result)
	}

	@Override
	AnnotationVisitor visitAnnotation(String descriptor, boolean visible)  {
		result.addDescriptor(descriptor)
		annotationVisitor
	}

	@Override
	AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
		visitAnnotation(descriptor, visible)
	}

	@Override
	void visitTypeInsn(int opcode, String descriptor) {
		if(descriptor.startsWith('[')) {
			result.addDescriptor(descriptor)
		} else {
			result.addClassName(descriptor)
		}
	}

	@Override
	void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
		result.addClassName(owner)
	}

	@Override
	void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
		result.addClassName(owner)
	}

	@Override
	void visitLdcInsn(Object constant) {
		if (constant instanceof Type) {
			result.addType(constant)
		}
	}

	@Override
	void visitMultiANewArrayInsn(String descriptor, int dims) {
		result.addDescriptor(descriptor)
	}

	@Override
	void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		result.addClassName(type)
	}

	@Override
	void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
		if(!signature) {
			result.addDescriptor(descriptor)
		} else {
			new SignatureReader(signature).acceptType(signatureVisitor)
		}
	}
}
