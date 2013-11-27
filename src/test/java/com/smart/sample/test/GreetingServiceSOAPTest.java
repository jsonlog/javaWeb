package com.smart.sample.test;

import com.smart.plugin.ws.SOAPHelper;
import com.smart.sample.ws.soap.GreetingService;
import org.junit.Test;

public class GreetingServiceSOAPTest {

    @Test
    public void sayHelloTest() {
        String wsdl = "http://localhost:8080/smart-sample/ws/GreetingService";
        GreetingService greetingService = SOAPHelper.createClient(wsdl, GreetingService.class);
        greetingService.sayHello("Jack");
    }
}
