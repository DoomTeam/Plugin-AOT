package com.uama.asm.method;

import com.uama.asm.StatisticsUtils;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.objectweb.asm.Opcodes.LSTORE;

/**
 * 我们在所有非构造函数方法中，插入一个方法，统计改方法启动时间，结束时间；耗时；
 * 方法名称，参数类型以及类名，存入该对象
 */
public class AddPointMethodAdapter extends MethodVisitor {
    public LocalVariablesSorter lvs;
    public AnalyzerAdapter aa;
    private int startTime;
    private int maxStack;
    private String className;
    private String methodName;
    private String methodDesc;

    public AddPointMethodAdapter(int api, int access, String methodName, String desc, String className, MethodVisitor mv) {
        super(api, mv);
        this.className = className;
        this.methodName = methodName;
        this.methodDesc = desc;
        lvs = new LocalVariablesSorter(access, desc, mv);
        aa = new AnalyzerAdapter(className, access, methodName, desc, mv);
    }

    @Override
    public void visitCode() {
        mv.visitCode();
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System",
                "currentTimeMillis", "()J", false);
        startTime = lvs.newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, startTime);
        maxStack = 4;
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
            //INVOKESTATIC class methodName desc 调用静态方法 此处无需从操作数栈取值
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System",
                    "currentTimeMillis", "()J", false);
//            //我们需要存入一个结束时间到局部变量中，此处复制一个操作数栈顶元素
//            mv.visitInsn(Opcodes.DUP);
            //获取一个LocalVariablesSorter 创建的局部变量地址
            int endTime = lvs.newLocal(Type.LONG_TYPE);
            //LSTORE 将操作数栈顶元素，存入endTime地址的局部变量中,释放栈顶，所以前面做了一个Dup
            mv.visitVarInsn(LSTORE, endTime);
            mv.visitVarInsn(Opcodes.LLOAD, endTime);
            //操作数栈压入局部变量：开始时间
            mv.visitVarInsn(Opcodes.LLOAD, startTime);
//            //LSUB 用于相减 end - start
            mv.visitInsn(Opcodes.LSUB);
//            //创建一个局部变量，保存我们方法耗时
            int useTime = lvs.newLocal(Type.LONG_TYPE);
//            //将耗时存入局部变量中
            mv.visitVarInsn(LSTORE, useTime);
//
//            //此处我们需要将一些数据放入我们的统计实体中了。。，瞬间根据构造函数，依次入栈
            mv.visitTypeInsn(Opcodes.NEW, "com/uama/asm/entity/Statistics");
            //栈顶复制+1
            mv.visitInsn(Opcodes.DUP);
//            //首先是类名
            mv.visitLdcInsn(className);
//            //方法名
            mv.visitLdcInsn(methodName);
//            //方法描述
            mv.visitLdcInsn(methodDesc);
//            //开始时间
            mv.visitVarInsn(Opcodes.LLOAD, startTime);
//            //结束时间
            mv.visitVarInsn(Opcodes.LLOAD, endTime);
//            //耗时
            mv.visitVarInsn(Opcodes.LLOAD, useTime);
//            //局部变量>=形参列表
            int maxParamsLength = aa.locals.size();
            if(maxParamsLength > 1){
                mv.visitLdcInsn(maxParamsLength);
                mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
                int params = lvs.newLocal(Type.getObjectType("[java/lang/Object"));
                mv.visitVarInsn(Opcodes.ASTORE, params);
                for(int i=0;i<maxParamsLength;i++){
                    mv.visitVarInsn(Opcodes.ALOAD, params);
                    mv.visitLdcInsn(i);
                    mv.visitVarInsn(Opcodes.ALOAD, i);
                    mv.visitInsn(Opcodes.AASTORE);
                }
                mv.visitVarInsn(Opcodes.ALOAD, params);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/uama/asm/entity/Statistics", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJJ[Ljava/lang/Object;)V", false);
            }else {
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/uama/asm/entity/Statistics", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJJ)V", false);
            }

            int obPoi = lvs.newLocal(Type.getObjectType("com/uama/asm/entity/Statistics"));
//            //存入该对象
            mv.visitVarInsn(Opcodes.ASTORE, obPoi);
//
            String key = StatisticsUtils.getKey(className, methodName, methodDesc);
            mv.visitVarInsn(Opcodes.ALOAD, obPoi);
            mv.visitLdcInsn(key);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/uama/asm/StatisticsUtils", "insertData", "(Lcom/uama/asm/entity/Statistics;Ljava/lang/String;)V", false);
//
            maxStack = Math.max(aa.stack.size() + 4, maxStack);
        }
        mv.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        mv.visitMaxs(Math.max(this.maxStack, maxStack), maxLocals);
    }

}