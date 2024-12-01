package site.hixview.support.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestContextManager;
import site.hixview.support.jdbc.callback.DynamicTestResetListener;

@Aspect
@TestConfiguration
public class TestResetListenerAspect {

    @Autowired
    private DynamicTestResetListener dynamicTestResetListener;

    @Pointcut("@annotation(site.hixview.support.bean.RegisterListenerConfig)")
    public void registerListenerPointcut() {
    }

    @Before("registerListenerPointcut()")
    public void registerDynamicTestResetListener(JoinPoint joinPoint) {
        Class<?> testClass = joinPoint.getTarget().getClass();
        TestContextManager testContextManager = new TestContextManager(testClass);
        testContextManager.registerTestExecutionListeners(dynamicTestResetListener);
    }
}
