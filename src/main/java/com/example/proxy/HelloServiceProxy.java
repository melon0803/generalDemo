package com.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 编写代理类，提供绑定和接口方法
 *
 * @author Carmelo
 * @date 2018/11/2 - 11:04
 * @since 1.0.0
 */
public class HelloServiceProxy implements InvocationHandler {
    /**
     * 真实的服务对象
     */
    private Object target;

    /**
     * 绑定一个委托对象并返回一个代理类
     *
     * @param target
     * @return
     */
    public Object bind(Object target) {
        this.target = target;
        Object proxyInstance = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
        return proxyInstance;
    }

    /**
     * 通过代理对象调用方法，首先进入这个方法
     *
     * @param proxy 代理对象
     * @param method 被调用方法
     * @param args 方法参数
     * @return
     * @throws Throwable
     */

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("我是JDK代理");
        Object object = null;
        //反射方法调用前
        System.out.println("开始调用方法");
        //执行方法，相当于调用HelloServiceImpl的sayHello()方法
        object = method.invoke(target,"world");
        //反射方法调用后
        System.out.println("方法调用结束");
        return object;
    }
}
