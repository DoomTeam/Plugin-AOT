package com.chenenyu.imgoptimizer.entity;

/**
 * 统计类
 */
public class Statistics {
    //类名
    private String className;
    //方法名
    private String methodName;
    //方法描述符()V
    private String methodDesc;
    //形参数值
    private Object[] params;
    //方法启动时间
    private long startTime;
    //方法结束时间
    private long endTime;
    //方法耗时
    private long useTime;

    public Statistics() {
    }

    public Statistics(String className, String methodName, String methodDesc, long startTime, long endTime, long useTime) {
        this.className = className;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.startTime = startTime;
        this.endTime = endTime;
        this.useTime = useTime;
    }

    public Statistics(String className, String methodName, String methodDesc) {
        this.className = className;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }

    public Statistics(String className, String methodName, String methodDesc, long startTime, long endTime, long useTime, Object[] params) {
        this.className = className;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.params = params;
        this.startTime = startTime;
        this.endTime = endTime;
        this.useTime = useTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }
}
