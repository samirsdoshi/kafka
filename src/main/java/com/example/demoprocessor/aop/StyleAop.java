package com.example.demoprocessor.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.GenericMessage;

@Aspect
@Configuration
public class StyleAop {
    @Before("execution(* com.example.demoprocessor.StyleService.processMessageCB(..))")
    public void before(JoinPoint joinPoint){
        //Advice
        System.out.println("in AOP:" );
        //((org.springframework.kafka.support.converter.KafkaMessageHeaders)((GenericMessage)((Object[])((CglibMethodInvocation)((MethodInvocationProceedingJoinPoint)joinPoint).methodInvocation).arguments)[0]).headers).entrySet().stream().forEach(k-> System.out.println(k))
    }
}
