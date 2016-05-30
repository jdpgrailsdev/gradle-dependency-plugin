package org.gradle.plugins.dependency.analyze.asm.visitor

import org.objectweb.asm.commons.Remapper

class ImportRecordingRemapper extends Remapper {

    Set<String> imports

    ImportRecordingRemapper(Set<String> imports) {
        this.imports = imports
    }

    @Override
    public String map(String typeName) {
        imports << typeName
    }
}