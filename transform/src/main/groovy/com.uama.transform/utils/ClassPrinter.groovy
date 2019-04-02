package com.uama.transform.utils

import org.objectweb.asm.Opcodes
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor

class ClassPrinter extends ClassVisitor{

    private boolean isFieldPresent
    ClassPrinter(ClassVisitor cv) {
        super(Opcodes.ASM5,cv)
    }

    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        System.out.println(name + " extends " + superName + " {")
    }

    public void visitSource(String source, String debug) {
    }
    public void visitOuterClass(String owner, String name, String desc)
    {
    }
    public FieldVisitor visitField(int access, String name, String
            desc,
                                   String signature, Object value) {
        System.out.println(" " + desc + " " + name)
        if (name == "hello") {
            isFieldPresent = true
        }
        return cv.visitField(access, name, desc, signature, value);
    }
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        System.out.println(" " + name + desc)
        return null
    }
    public void visitEnd() {
        System.out.println("}")
        if (!isFieldPresent) {
            FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC, "hello", "I", null, null);
            if (fv != null) {
                fv.visitEnd()
            }
        }
        cv.visitEnd()
    }
}