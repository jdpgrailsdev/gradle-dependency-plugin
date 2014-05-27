package org.gradle.plugins.dependency.analyze.asm.visitor

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.signature.SignatureReader
import org.objectweb.asm.signature.SignatureVisitor

class ASMClassVisitor extends ClassVisitor {

	private AnnotationVisitor annotationVisitor
	private FieldVisitor fieldVisitor
	private MethodVisitor methodVisitor
	private SignatureVisitor signatureVisitor
	private ASMVisitorResult result

	ASMClassVisitor(ASMVisitorResult result) {
		super(Opcodes.ASM5)
		this.result = result ?: new ASMVisitorResult()
		this.annotationVisitor = new ASMAnnotationVisitor(this.result)
		this.fieldVisitor = new ASMFieldVisitor(this.annotationVisitor, this.result)
		this.signatureVisitor = new ASMSignatureVisitor(this.result)
		this.methodVisitor = new ASMMethodVisitor(this.annotationVisitor, this.signatureVisitor, this.result)
	}

	@Override
	void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		if(!signature) {
			result.addClassName(superName)
			result.addClassNames(interfaces)
		} else {
			new SignatureReader(signature).accept(signatureVisitor)
		}

	}

	@Override
	AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		result.addDescriptor(descriptor)
		annotationVisitor
	}

	@Override
	FieldVisitor visitField(int access, String className, String descriptor, String signature, Object value) {
		if(!signature) {
			result.addDescriptor(descriptor)
		} else {
			new SignatureReader(signature).acceptType(signatureVisitor)
		}

		if (value instanceof Type) {
			result.addType(value)
		}

		fieldVisitor
	}

	@Override
	public MethodVisitor visitMethod(int access, String className, String descriptor, String signature, String[] exceptions) {
		if(!signature) {
			result.addMethodDescriptor(descriptor)
		} else {
			new SignatureReader(signature).accept(signatureVisitor)
		}

		result.addClassNames(exceptions)

		methodVisitor
	}
}