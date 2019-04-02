package com.uama.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM6;

public class AddFiledAdapter extends ClassVisitor {
    private int access;
    private String name;
    private String desc;

    private boolean hasExit = false;
    public AddFiledAdapter(ClassVisitor classVisitor, int access,  String name, String desc){
        super(ASM6,classVisitor);
        this.access = access;
        this.name = name;
        this.desc = desc;
    }

    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        if(this.access == access&&this.name.equals(name)&&this.desc.equals(desc)){
            hasExit = true;
        }
        if (cv != null) {
            return cv.visitField(access, name, desc, signature, value);
        }
        return null;
    }

    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        if (cv != null) {
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }
        return null;
    }


    public void visitEnd() {
        if(!hasExit){
            FieldVisitor fv = cv.visitField(access,name,desc,null,null);
            if(fv!=null)fv.visitEnd();
        }

        if (cv != null) {
            cv.visitEnd();
        }
    }

}
