package org.gradle.plugins.dependency.analyze.asm;
package org.gradle.plugins.dependency.anaylze.asm

class ASMClassAnalyzer {

	private ResultCollector resultCollector
	private SignatureVisitor signatureVisitor
	private AnnotationVisitor annotationVisitor
	private FieldVisitor fieldVisitor
	private MethodVisitor methodVisitor

	ASMClassAnalyzer() {

		// constructors -----------------------------------------------------------

		public DefaultClassVisitor(SignatureVisitor signatureVisitor, AnnotationVisitor annotationVisitor, FieldVisitor fieldVisitor, MethodVisitor methodVisitor, ResultCollector resultCollector)
		{
			super(Opcodes.ASM5);
			this.signatureVisitor = signatureVisitor;
			this.annotationVisitor = annotationVisitor;
			this.fieldVisitor = fieldVisitor;
			this.methodVisitor = methodVisitor;
			this.resultCollector = resultCollector;
		}

		public void visit( final int version, final int access, final String name, final String signature,
		final String superName, final String[] interfaces )
		{
			if ( signature == null )
			{
				resultCollector.addName(superName);
				resultCollector.addNames(interfaces);
			}
			else
			{
				addSignature( signature );
			}
		}

		public AnnotationVisitor visitAnnotation( final String desc, final boolean visible )
		{
			resultCollector.addDesc(desc);

			return annotationVisitor;
		}

		public FieldVisitor visitField( final int access, final String name, final String desc, final String signature,
		final Object value )
		{
			if ( signature == null )
			{
				resultCollector.addDesc(desc);
			}
			else
			{
				addTypeSignature(signature);
			}

			if ( value instanceof Type )
			{
				resultCollector.addType((Type) value);
			}

			return fieldVisitor;
		}

		public MethodVisitor visitMethod( final int access, final String name, final String desc, final String signature,
		final String[] exceptions )
		{
			if ( signature == null )
			{
				resultCollector.addMethodDesc(desc);
			}
			else
			{
				addSignature(signature);
			}

			resultCollector.addNames( exceptions );

			return methodVisitor;
		}


		// private methods --------------------------------------------------------

		private void addSignature( final String signature )
		{
			if ( signature != null )
			{
				new SignatureReader( signature ).accept( signatureVisitor );
			}
		}

		private void addTypeSignature( final String signature )
		{
			if ( signature != null )
			{
				new SignatureReader( signature ).acceptType( signatureVisitor );
			}
		}
	}

}
