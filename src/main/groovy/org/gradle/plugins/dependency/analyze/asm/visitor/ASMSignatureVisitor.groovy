package org.gradle.plugins.dependency.analyze.asm.visitor

import org.objectweb.asm.Opcodes
import org.objectweb.asm.signature.SignatureVisitor

class ASMSignatureVisitor extends SignatureVisitor {

	private ASMVisitorResult result

	ASMSignatureVisitor(ASMVisitorResult result) {
		super(Opcodes.ASM5)
		this.result = result ?: new ASMVisitorResult()
	}

	@Override
	void visitClassType(String className) {
		result.addClassName(className)
	}

	@Override
	void visitInnerClassType(String className) {
		visitClassType(className)
	}
}
