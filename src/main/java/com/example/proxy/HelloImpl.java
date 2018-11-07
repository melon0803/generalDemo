package com.example.proxy;

/**
 * cglib动态代理实现类
 *
 * @author Carmelo
 * @date 2018/11/2 - 15:05
 * @since 1.0.0
 */
public class HelloImpl {
    public void sayHello(String str) {
        System.out.println("hello"+str);
    }
}
