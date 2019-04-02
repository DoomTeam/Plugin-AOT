package com.uama.asm;


import com.uama.asm.method.AddPointMethodAdapter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.CheckMethodAdapter;

import static org.objectweb.asm.Opcodes.ACC_INTERFACE;

/**
 * 添加埋点的适配器
 */
public class AddPointAdapter extends ClassVisitor {
    public AddPointAdapter(int api, ClassVisitor cv) {
        super(api, cv);
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
            mv = new AddPointMethodAdapter(Opcodes.ASM6,access,name, desc,owner,mv);
            mv= new CheckMethodAdapter(mv);
        }
        return mv;
    }
}
