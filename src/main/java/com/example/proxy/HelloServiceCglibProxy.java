package com.example.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 实现cglib的动态代理
 *
 * @author Carmelo
 * @date 2018/11/2 - 14:48
 * @since 1.0.0
 */
public class HelloServiceCglibProxy implements MethodInterceptor {

    private Object target;

    /**
     * 创建代理对象
     *
     * @param target
     * @return
     */
    public Object getInstance (Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        //回调方法
        enhancer.setCallback(this);
        //创建代理对象
        return enhancer.create();
    }

    /**
     *
     * @param o
     * @param method
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("我是cglib动态代理");

        //反射方法调用前
        System.out.println("开始调用");
        Object returnObj = methodProxy.invokeSuper(o, objects);
        //反射方法调用后
        System.out.println("调用完成");

        return returnObj;
    }
}
