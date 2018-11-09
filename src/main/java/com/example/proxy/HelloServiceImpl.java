package com.example.proxy;

/**
 * @author Carmelo
 * @date 2018/11/2 - 11:03
 * @since 1.0.0
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public void sayHello(String str) {
        System.out.println("hello"+str);
    }

    @Override
    public void run(String str) {
        System.out.println("guys"+str);
    }
}
