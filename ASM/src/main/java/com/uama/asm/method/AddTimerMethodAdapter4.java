package com.uama.asm.method;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.RETURN;

class AddTimerMethodAdapter4 extends LocalVariablesSorter {
    private int time;
    private String owner;

    public AddTimerMethodAdapter4(int api, int access,String owner, String desc,
                                  MethodVisitor mv) {
        super(api, access, desc, mv);
        this.owner = owner;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System",
                "currentTimeMillis", "()J", false);
        time = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, time);
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System",
                    "currentTimeMillis", "()J", false);
            mv.visitVarInsn(Opcodes.LLOAD, time);
            mv.visitInsn(Opcodes.LSUB);
            mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "timer", "J");
            mv.visitInsn(Opcodes.LADD);
            mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "timer", "J");
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 4, maxLocals);
    }
}