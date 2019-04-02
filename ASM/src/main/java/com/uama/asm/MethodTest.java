package com.uama.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;


/**
 * 测试methodVisitor
 */
public class MethodTest {
    public static void main(String[] args){
        ClassWriter classWriter = new ClassWriter(0);
        try {
            ClassReader classReader = new ClassReader("com.uama.asm.MethodWaitUpdate");
            //此工具类将class文件进一步转化成asm 代码
            ASMifier asMifier = new ASMifier();
            PrintWriter printWriter = new PrintWriter("E:\\help\\method.txt");
            //设置一个帅气的输出目录，可以查看文件轨迹:
            TraceClassVisitor classPrinter = new TraceClassVisitor(classWriter,asMifier,printWriter);
//            CheckClassAdapter checkClassAdapter = new CheckClassAdapter(classPrinter);
            AddPointAdapter addPointAdapter = new AddPointAdapter(Opcodes.ASM6,classPrinter);

            classReader.accept(addPointAdapter,0);

            String pag = "com\\uama\\asm";
            File dir = new File("F:\\zero-android\\matrix-master\\samples\\TestPlugin\\ASM",pag);
            if(dir.mkdirs()||dir.exists()){
                File file=new File(dir,"MethodWaitUpdate.class");
                byte[] by=classWriter.toByteArray();
                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                fos.write(by);
                fos.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
