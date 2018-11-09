package com.example.exceldemo;

import com.example.proxy.HelloImpl;
import com.example.proxy.HelloServiceCglibProxy;
import org.junit.Test;

/**
 * @author Carmelo
 * @date 2018/11/2 - 14:54
 * @since 1.0.0
 */
public class CglibProxyTest {
    @Test
    public void testCglib() {
        HelloServiceCglibProxy helloServiceCglibProxy = new HelloServiceCglibProxy();
        HelloImpl helloService = (HelloImpl) helloServiceCglibProxy.getInstance(new HelloImpl());
        helloService.sayHello("123");
    }
}
