package com.uama.asm;

public class HelloAsm {
    private final static String boy = "111";
    private static int f;
    public void sayHello(){
        f =10;
        System.out.println("999");
    }

    public void checkAndSetF(int f) {
        if (f >= 1) {
            this.f = f;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
