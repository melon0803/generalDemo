package com.example.exceldemo;

import com.example.proxy.HelloService;
import com.example.proxy.HelloServiceImpl;
import com.example.proxy.HelloServiceProxy;
import org.junit.Test;

/**
 * 代理测试类
 *
 * @author Carmelo
 * @date 2018/11/2 - 11:21
 * @since 1.0.0
 */
public class ProxyTest {
    @Test
    public void testProxy() {
        HelloServiceProxy helloServiceProxy = new HelloServiceProxy();

        HelloService helloService = (HelloService) helloServiceProxy.bind(new HelloServiceImpl());
        //调用不同的代理对象的方法，就会运行接口对应不同的方法实现
        helloService.run("hey");
        helloService.sayHello("12345");
    }
}
