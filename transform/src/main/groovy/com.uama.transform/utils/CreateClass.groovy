package com.uama.transform.utils

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

/**
 * package pkg;
 * public interface Comparable extends Mesurable {* int LESS = -1;
 * int EQUAL = 0;
 * int GREATER = 1;
 * int compareTo(Object o);
 * }
 *
 * visit visitSource? visitOuterClass? ( visitAnnotation |
 * visitAttribute )*
 * ( visitInnerClass | visitField | visitMethod )*
 * visitEnd
 */
class CreateClass {

    public static byte[] createClass(){
        ClassWriter cw = new ClassWriter(0)
        cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,"pkg/Comparable",null,"java/lang/Object",null);
        cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "LESS", "I",
                null, new Integer(-1)).visitEnd();
        cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "EQUAL", "I",
                null, new Integer(0)).visitEnd();
        cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "GREATER", "I",
                null, new Integer(1)).visitEnd();
        cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "compareTo",
                "(Ljava/lang/Object;)I", null, null).visitEnd();
        cw.visitEnd()
        return cw.toByteArray()
    }
}