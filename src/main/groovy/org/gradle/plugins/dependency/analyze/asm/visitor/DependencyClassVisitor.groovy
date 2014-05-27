package org.gradle.plugins.dependency.analyze.asm.visitor

import org.gradle.api.tasks.TaskExecutionException
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor

class DependencyClassVisitor {

    static final CLASS_READER_FLAGS = 0

    private ASMVisitorResult result = new ASMVisitorResult()

    void visitClass(File classFile) {
        try {
            if(classFile && classFile.exists()) {
                ClassReader classReader = new ClassReader(classFile.newInputStream())
                classReader.accept(new ASMClassVisitor(result), CLASS_READER_FLAGS)
            } else {
                throw new IllegalArgumentException("Unable to inspect class file: ${classFile ? "class file '${classFile?.absolutePath}' does not exist." : "classFile cannot be null."}")
            }
        } catch (e) {
            throw new IllegalArgumentException("Unable to inspect class file '${classFile?.absolutePath}'.", e)
        }
    }

    Set<String> getDependencyClasses() {
        result.getDependencyClasses()
    }
}
