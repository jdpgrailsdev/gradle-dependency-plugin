package org.gradle.plugins.dependency.analyze.asm.visitor

import java.util.regex.Matcher

import org.objectweb.asm.Type

class ASMVisitorResult {

    private Set<String> dependencyClasses = new HashSet<String>()

    void addClassName(String className) {
        if(className) {
            Matcher matcher = className =~ /^\[L(.+);$/
            if(matcher.matches()) {
                className = matcher[0][1]
            }
            dependencyClasses << className.replaceAll('/', '.')
        }
    }

    void addClassNames(final String[] classNames) {
        classNames?.each { String className ->
            addClassName(className)
        }
    }

    void addDescriptor(final String descriptor) {
        addType(Type.getType(descriptor))
    }

    void addType(final Type type) {
        switch(type.getSort()) {
            case Type.ARRAY:
                addType(type.getElementType())
                break
            case Type.OBJECT:
                addClassName(type.getClassName().replaceAll( '\\.', '/' ))
                break
        }
    }

    void addMethodDescriptor(String methodDescriptor) {
        addType(Type.getReturnType(methodDescriptor))

        Type.getArgumentTypes(methodDescriptor)?.each { Type type ->
            addType(type)
        }
    }

    Set<String> getDependencyClasses() {
        dependencyClasses
    }
}