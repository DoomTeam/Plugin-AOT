package com.uama.testplugin;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {

    public static void main(String args[]){
        createClass();
    }
    public static byte[] createClass(){
        ClassWriter cw = new ClassWriter(0);
        cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,"pkg/Comparable",null,"java/lang/Object",new String[]{"pkg/Mesurable"});
        cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "LESS", "I",
                null, new Integer(-1)).visitEnd();
        cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "EQUAL", "I",
                null, new Integer(0)).visitEnd();
        cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "GREATER", "I",
                null, new Integer(1)).visitEnd();
        cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "compareTo",
                "(Ljava/lang/Object;)I", null, null).visitEnd();
        cw.visitEnd();
        byte[] bytes= cw.toByteArray();
        File file=new File("F:\\zero-android\\matrix-master\\samples\\TestPlugin\\app\\build\\pkg\\Comparable.class");
        try {
            if(file.createNewFile()){
                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                fos.write(bytes);
                fos.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return  bytes;
    }
}
