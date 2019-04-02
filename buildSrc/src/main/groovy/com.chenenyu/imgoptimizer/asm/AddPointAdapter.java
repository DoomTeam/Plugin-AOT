package com.chenenyu.imgoptimizer.asm;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.util.CheckMethodAdapter;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;

/**
 * 添加埋点的适配器
 */
public class AddPointAdapter extends ClassVisitor {
    public AddPointAdapter(ClassVisitor cv) {
        super(Opcodes.ASM6, cv);
    }

    private String owner;
    private boolean isInterface = false;

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        isInterface = (access & ACC_INTERFACE) != 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null && !isInterface && !name.equals("<init>")) {
            AddPointMethodAdapter  at = new AddPointMethodAdapter(Opcodes.ASM6,name, desc,owner,mv);
            at.aa = new AnalyzerAdapter(owner, access, name, desc, at);
            at.lvs = new LocalVariablesSorter(access, desc, at.aa);
            return at.lvs;
        }
        return mv;
    }
}
