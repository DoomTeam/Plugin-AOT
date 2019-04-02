package com.uama.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;


import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ASM6;


public class MyClass {

    public static void main(String[] args){
        try {

            updateMethod();
            //此处通过classReader读取一个类
            ClassReader classReader =new ClassReader("com.uama.asm.HelloAsm");
            //此处定义一个阅读解析ClassVisitor类的阅读器
//            ClassWriter classWriter = new ClassWriter(0);
            ClassPrinter classPrinter1 = new ClassPrinter();
            PrintWriter printWriter = new PrintWriter("E:\\help\\hello.txt");
            //此工具类将class文件进一步转化成asm 代码
            ASMifier asMifier = new ASMifier();
            //设置一个帅气的输出目录，可以查看文件轨迹:
            TraceClassVisitor classPrinter = new TraceClassVisitor(classPrinter1,asMifier,printWriter);
//            CheckClassAdapter checkClassAdapter = new CheckClassAdapter(classPrinter);
            RemoveMethodAdapter removeMethodAdapter = new RemoveMethodAdapter(classPrinter,"sayHello","()V");
            AddFiledAdapter addFiledAdapter = new AddFiledAdapter(removeMethodAdapter,ACC_PUBLIC,"addValue","Ljava/lang/String");
            AddTimerAdapter addTimerAdapter = new AddTimerAdapter(addFiledAdapter);
            //将classReader读取的信息传递给ClassPrinter，由阅读器翻译
            // 看下链条-》reader数据->RemoveMethodAdapter->ClassPrinter->classWriter获取byte字节
            classReader.accept(addTimerAdapter,0);

            ClassReader classReader1 =new ClassReader(writeClass());
            classReader1.accept(classPrinter,0);

            System.out.println("excute");
            HelloAsm helloAsm = new HelloAsm();
            helloAsm.sayHello();

        }catch (IOException e){
            System.out.println("IOException e:"+e.getMessage());
            e.printStackTrace();
        }
    }

    private static void updateMethod() throws IOException {
        try {
            ClassWriter classWriter = new ClassWriter(0);
            AddTimerAdapter addTimerAdapter =new AddTimerAdapter(classWriter);
            ClassReader classReader = new ClassReader("com.uama.asm.TestMethod");
            classReader.accept(addTimerAdapter,0);
            MyClassLoader loader = new MyClassLoader();
            Class c = loader.defineClass("com.uama.asm.TestMethod", classWriter.toByteArray());
            System.out.println("Class c:"+c.getName());
            Object o = c.newInstance();
            Method method = c.getDeclaredMethod("sleep");
            method.invoke(o);

            String pag = "com\\uama\\asm";
            File dir = new File("F:\\zero-android\\matrix-master\\samples\\TestPlugin\\ASM",pag);
            if(dir.mkdirs()){
                File file=new File(dir,"TestMethod.class");
                byte[] by=classWriter.toByteArray();
                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                fos.write(by);
                fos.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 此处我们简单的通过classWriter去写了一个类
     * @return 所写类对应的字节数组
     */
    private static  byte[] writeClass(){
        //创建文件抒写类
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(Opcodes.V1_7,Opcodes.ACC_PUBLIC+ ACC_ABSTRACT + ACC_INTERFACE,"pkg/Comparable",
                null,"java/lang/Object",null);
        classWriter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,"LESS","I",null,-1);
        classWriter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,"EQUAL","I",null,0).visitEnd();
        classWriter.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,"GREATER","I",null,1).visitEnd();
        classWriter.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT ,"compareTo","(Ljava/lang/Object;)I",null,null).visitEnd();
        classWriter.visitEnd();
        byte[] b = classWriter.toByteArray();
        MyClassLoader loader = new MyClassLoader();
        Class c = loader.defineClass("pkg.Comparable", b);
        System.out.println("Class c:"+c.getName());
        return b;
    }

    private static void copyByte(byte[] bytes){
        ClassReader classReader = new ClassReader(bytes);
        //编写器
        ClassWriter classWriter = new ClassWriter(classReader,0);
        //适配器 信息交给编写器
        ClassVisitor classVisitor = new ChangeVersionAdapter();
        //reader 交给 适配器
        classReader.accept(classVisitor,0);
        classWriter.toByteArray();
    }

    static class MyClassLoader extends ClassLoader{
        public Class defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader l, String name, Class c,
                                    ProtectionDomain d, byte[] b)
                    throws IllegalClassFormatException {
                ClassReader cr = null;
                try {
                    cr = new ClassReader(agentArgs);
                    ClassWriter cw = new ClassWriter(cr, 0);
                    ClassVisitor cv = new ChangeVersionAdapter();
                    cr.accept(cv, 0);
                    return cw.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    static class ChangeVersionAdapter extends ClassVisitor {

        public ChangeVersionAdapter() {
            super(ASM6);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(Opcodes.V1_7, access, name, signature, superName, interfaces);
        }
    }
}
